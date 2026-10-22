package com.nativelap.ecoguard.core.network

import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.CancellationException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

class ApiExecutorTest {
    private val apiExecutor = ApiExecutor()

    @Test
    fun `execute returns response body on success`() =
        runSuspendTest {
            val result = apiExecutor.execute { Response.success("ok") }

            assertEquals(NetworkResult.Success("ok"), result)
        }

    @Test
    fun `execute maps HTTP errors to status code`() =
        runSuspendTest {
            val result =
                apiExecutor.execute {
                    Response.error<String>(401, "unauthorized".toResponseBody())
                }

            assertEquals(NetworkResult.Failure(NetworkError.Http(401)), result)
        }

    @Test
    fun `execute maps IO failures to connectivity error`() =
        runSuspendTest {
            val result = apiExecutor.execute<String> { throw IOException("offline") }

            assertEquals(NetworkResult.Failure(NetworkError.Connectivity), result)
        }

    @Test
    fun `execute maps serialization failures to invalid response`() =
        runSuspendTest {
            val result = apiExecutor.execute<String> { throw SerializationException("invalid JSON") }

            assertEquals(NetworkResult.Failure(NetworkError.InvalidResponse), result)
        }

    @Test
    fun `execute preserves coroutine cancellation`() =
        runSuspendTest {
            val cancellation = CancellationException("cancelled")

            try {
                apiExecutor.execute<String> { throw cancellation }
                throw AssertionError("Expected cancellation to propagate")
            } catch (actual: CancellationException) {
                assertSame(cancellation, actual)
            }
        }

    @Test
    fun `executeNoContent treats successful empty response as success`() =
        runSuspendTest {
            val result = apiExecutor.executeNoContent { Response.success(204, Unit) }

            assertEquals(NetworkResult.Success(Unit), result)
        }

    @Test
    fun `executeNoContent maps HTTP errors to status code`() =
        runSuspendTest {
            val result =
                apiExecutor.executeNoContent {
                    Response.error<Unit>(403, "forbidden".toResponseBody())
                }

            assertEquals(NetworkResult.Failure(NetworkError.Http(403)), result)
        }
}

private fun String.toResponseBody(): okhttp3.ResponseBody = toResponseBody("application/json".toMediaType())

private fun <T> runSuspendTest(block: suspend () -> T): T {
    var result: Result<T>? = null
    val completed = CountDownLatch(1)
    block.startCoroutine(
        object : Continuation<T> {
            override val context = EmptyCoroutineContext

            override fun resumeWith(resumeResult: Result<T>) {
                result = resumeResult
                completed.countDown()
            }
        },
    )
    check(completed.await(5, TimeUnit.SECONDS)) { "Suspend test did not complete" }
    return checkNotNull(result).getOrThrow()
}
