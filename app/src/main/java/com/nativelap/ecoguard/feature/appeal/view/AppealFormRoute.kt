package com.nativelap.ecoguard.feature.appeal.view

import android.graphics.BitmapFactory
import android.media.ExifInterface
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormUiState
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormViewModel
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealScreenEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

private const val MAX_APPEAL_PHOTO_COUNT = 3
private const val MAX_APPEAL_PHOTO_BYTES = 10L * 1024L * 1024L
private const val COPY_BUFFER_BYTES = 16 * 1024

@Composable
fun AppealFormRoute(
    verificationId: Long,
    initialUiState: AppealFormUiState,
    onEvent: (AppealScreenEvent) -> Unit = {},
    photos: List<File> = emptyList(),
    onSubmitted: () -> Unit = {},
    viewModel: AppealFormViewModel? = null,
) {
    val screenViewModel = viewModel ?: rememberActivityViewModel<AppealFormViewModel>()
    val uiState by screenViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val photoPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            scope.launch {
                val copiedPhotos = mutableListOf<File>()
                val results = mutableListOf<AppealPhotoCopyResult>()
                try {
                    uris.take(MAX_APPEAL_PHOTO_COUNT).forEach { uri ->
                        val result = copyAppealPhoto(context.contentResolver, uri, context.cacheDir)
                        when (result) {
                            is AppealPhotoCopyResult.Copied -> copiedPhotos += result.file
                            else -> Unit
                        }
                        results += result
                    }
                } catch (exception: CancellationException) {
                    copiedPhotos.forEach(File::delete)
                    throw exception
                }
                if (copiedPhotos.isNotEmpty()) {
                    screenViewModel.updatePhotos(copiedPhotos)
                }
                if (results.any { it == AppealPhotoCopyResult.TooLarge }) {
                    android.widget.Toast
                        .makeText(
                            context,
                            R.string.appeal_photo_too_large,
                            android.widget.Toast.LENGTH_LONG,
                        ).show()
                } else if (results.any { it == AppealPhotoCopyResult.Invalid }) {
                    android.widget.Toast
                        .makeText(
                            context,
                            R.string.appeal_photo_copy_failed,
                            android.widget.Toast.LENGTH_LONG,
                        ).show()
                }
            }
        }
    BackHandler {
        if (uiState?.isSubmitting != true) {
            screenViewModel.clearPhotos()
            onEvent(AppealScreenEvent.BackClick)
        }
    }

    LaunchedEffect(screenViewModel, verificationId) {
        screenViewModel.initialize(verificationId, initialUiState, photos)
    }

    LaunchedEffect(uiState?.isSubmitted) {
        if (uiState?.isSubmitted == true) onSubmitted()
    }

    AppealFormScreen(
        uiState = uiState ?: initialUiState,
        onEvent = { screenEvent ->
            when (screenEvent) {
                is AppealScreenEvent.ContentChange -> {
                    screenViewModel.updateContent(screenEvent.appealContent)
                }

                AppealScreenEvent.SubmitClick,
                AppealScreenEvent.RetrySendClick,
                -> {
                    screenViewModel.submit()
                }

                AppealScreenEvent.AddPhotoClick -> {
                    if (uiState?.let { !it.isSubmitting && !it.isSubmitted } == true) photoPicker.launch("image/*")
                }

                AppealScreenEvent.BackClick -> {
                    if (uiState?.isSubmitting != true) {
                        screenViewModel.clearPhotos()
                        onEvent(screenEvent)
                    }
                }

                else -> {
                    onEvent(screenEvent)
                }
            }
        },
    )
}

private sealed interface AppealPhotoCopyResult {
    data class Copied(
        val file: File,
    ) : AppealPhotoCopyResult

    data object TooLarge : AppealPhotoCopyResult

    data object Invalid : AppealPhotoCopyResult
}

internal class AppealPhotoTooLargeException : Exception()

private suspend fun copyAppealPhoto(
    contentResolver: android.content.ContentResolver,
    uri: android.net.Uri,
    cacheDirectory: File,
): AppealPhotoCopyResult =
    withContext(Dispatchers.IO) {
        var photoFile: File? = null
        try {
            val mimeType = contentResolver.getType(uri)
            val extension =
                when (mimeType) {
                    "image/jpeg" -> "jpg"
                    "image/png" -> "png"
                    "image/webp" -> "webp"
                    else -> return@withContext AppealPhotoCopyResult.Invalid
                }
            val copiedFile = File.createTempFile("appeal-photo-", ".$extension", cacheDirectory)
            photoFile = copiedFile
            val input =
                contentResolver.openInputStream(uri) ?: throw IllegalStateException("Selected photo cannot be opened")
            input.use { source ->
                copiedFile.outputStream().buffered().use { output ->
                    source.copyBoundedTo(output, MAX_APPEAL_PHOTO_BYTES)
                }
            }
            if (!copiedFile.hasImageSignature(mimeType) || !copiedFile.hasDecodableImage()) {
                throw IllegalStateException("Selected photo is not a supported image")
            }
            if (!copiedFile.removeLocationMetadata()) {
                throw IllegalStateException("Selected photo metadata could not be removed")
            }
            AppealPhotoCopyResult.Copied(copiedFile)
        } catch (_: AppealPhotoTooLargeException) {
            photoFile?.delete()
            AppealPhotoCopyResult.TooLarge
        } catch (exception: CancellationException) {
            photoFile?.delete()
            throw exception
        } catch (_: Exception) {
            photoFile?.delete()
            AppealPhotoCopyResult.Invalid
        }
    }

internal fun InputStream.copyBoundedTo(
    output: java.io.OutputStream,
    maxBytes: Long,
) {
    val buffer = ByteArray(COPY_BUFFER_BYTES)
    var copiedBytes = 0L
    while (true) {
        val readCount = read(buffer)
        if (readCount < 0) return
        copiedBytes += readCount
        if (copiedBytes > maxBytes) throw AppealPhotoTooLargeException()
        output.write(buffer, 0, readCount)
    }
}

internal fun File.hasImageSignature(mimeType: String): Boolean {
    val header = ByteArray(12)
    val readCount = inputStream().use { it.read(header) }
    val signatureMatches =
        when (mimeType) {
            "image/jpeg" -> {
                readCount >= 3 && header[0] == 0xFF.toByte() && header[1] == 0xD8.toByte() &&
                    header[2] == 0xFF.toByte()
            }

            "image/png" -> {
                readCount >= 8 &&
                    header.take(8).map { it.toInt() and 0xFF } == listOf(137, 80, 78, 71, 13, 10, 26, 10)
            }

            "image/webp" -> {
                readCount >= 12 && String(header, 0, 4, Charsets.US_ASCII) == "RIFF" &&
                    String(header, 8, 4, Charsets.US_ASCII) == "WEBP"
            }

            else -> {
                false
            }
        }
    return signatureMatches
}

private fun File.hasDecodableImage(): Boolean {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(absolutePath, options)
    return options.outWidth > 0 && options.outHeight > 0
}

private fun File.removeLocationMetadata(): Boolean =
    runCatching {
        ExifInterface(this).apply {
            GPS_METADATA_TAGS.forEach { setAttribute(it, null) }
            saveAttributes()
        }
    }.isSuccess

private val GPS_METADATA_TAGS =
    listOf(
        ExifInterface.TAG_GPS_LATITUDE,
        ExifInterface.TAG_GPS_LATITUDE_REF,
        ExifInterface.TAG_GPS_LONGITUDE,
        ExifInterface.TAG_GPS_LONGITUDE_REF,
        ExifInterface.TAG_GPS_ALTITUDE,
        ExifInterface.TAG_GPS_ALTITUDE_REF,
        ExifInterface.TAG_GPS_TIMESTAMP,
        ExifInterface.TAG_GPS_DATESTAMP,
        ExifInterface.TAG_GPS_PROCESSING_METHOD,
        ExifInterface.TAG_GPS_AREA_INFORMATION,
        ExifInterface.TAG_GPS_DEST_LATITUDE,
        ExifInterface.TAG_GPS_DEST_LATITUDE_REF,
        ExifInterface.TAG_GPS_DEST_LONGITUDE,
        ExifInterface.TAG_GPS_DEST_LONGITUDE_REF,
        ExifInterface.TAG_GPS_DEST_BEARING,
        ExifInterface.TAG_GPS_DEST_BEARING_REF,
        ExifInterface.TAG_GPS_DEST_DISTANCE,
        ExifInterface.TAG_GPS_DEST_DISTANCE_REF,
        ExifInterface.TAG_GPS_DIFFERENTIAL,
        ExifInterface.TAG_GPS_MAP_DATUM,
        ExifInterface.TAG_GPS_MEASURE_MODE,
        ExifInterface.TAG_GPS_SATELLITES,
        ExifInterface.TAG_GPS_SPEED,
        ExifInterface.TAG_GPS_SPEED_REF,
        ExifInterface.TAG_GPS_STATUS,
        ExifInterface.TAG_GPS_TRACK,
        ExifInterface.TAG_GPS_TRACK_REF,
    )
