package com.nativelap.ecoguard.feature.notification.viewmodel

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class NotificationMutationQueue(
    private val onPendingChanged: (Boolean) -> Unit,
) {
    private val mutex = Mutex()
    private val stateLock = Any()
    private var pendingCount = 0

    suspend fun <T> enqueue(action: suspend () -> T): T {
        updatePendingCount(1)
        return try {
            mutex.withLock { action() }
        } finally {
            updatePendingCount(-1)
        }
    }

    private fun updatePendingCount(change: Int) {
        synchronized(stateLock) {
            pendingCount += change
            onPendingChanged(pendingCount > 0)
        }
    }
}
