package com.nativelap.ecoguard.feature.notification

import com.nativelap.ecoguard.feature.notification.viewmodel.NotificationMutationQueue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger

class NotificationMutationQueueTest {
    @Test
    fun `queues each mutation and keeps busy state until all actions finish`() =
        runBlocking {
            val busyStates = Collections.synchronizedList(mutableListOf<Boolean>())
            val pendingCount = AtomicInteger()
            val bothActionsQueued = CompletableDeferred<Unit>()
            val firstActionStarted = CompletableDeferred<Unit>()
            val releaseFirstAction = CompletableDeferred<Unit>()
            val actionOrder = Collections.synchronizedList(mutableListOf<String>())
            val queue =
                NotificationMutationQueue { isBusy ->
                    busyStates += isBusy
                    if (pendingCount.incrementAndGet() == 2) bothActionsQueued.complete(Unit)
                }

            val firstAction =
                async(Dispatchers.Default) {
                    queue.enqueue {
                        actionOrder += "first-start"
                        firstActionStarted.complete(Unit)
                        releaseFirstAction.await()
                        actionOrder += "first-end"
                    }
                }
            firstActionStarted.await()
            val secondAction =
                async(Dispatchers.Default) {
                    queue.enqueue { actionOrder += "second" }
                }
            bothActionsQueued.await()

            assertEquals(listOf("first-start"), actionOrder)
            assertTrue(busyStates.all { it })

            releaseFirstAction.complete(Unit)
            firstAction.await()
            secondAction.await()

            assertEquals(listOf("first-start", "first-end", "second"), actionOrder)
            assertFalse(busyStates.last())
        }

    @Test
    fun `clears busy state and continues queued work after a failure`() =
        runBlocking {
            val busyStates = Collections.synchronizedList(mutableListOf<Boolean>())
            val queue = NotificationMutationQueue { isBusy -> busyStates.add(isBusy) }
            val firstActionStarted = CompletableDeferred<Unit>()
            val releaseFirstAction = CompletableDeferred<Unit>()
            var secondActionRan = false

            val failedAction =
                async(Dispatchers.Default) {
                    runCatching {
                        queue.enqueue {
                            firstActionStarted.complete(Unit)
                            releaseFirstAction.await()
                            error("expected failure")
                        }
                    }
                }
            firstActionStarted.await()
            val nextAction =
                async(Dispatchers.Default) {
                    queue.enqueue { secondActionRan = true }
                }
            releaseFirstAction.complete(Unit)
            failedAction.await()
            nextAction.await()

            assertTrue(secondActionRan)
            assertFalse(busyStates.last())
        }
}
