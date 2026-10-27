package com.nativelap.ecoguard.feature.verification.view

import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import java.io.File
import java.util.UUID

/** Opens the device camera app and retains the destination file across activity recreation. */
@Composable
fun CameraCaptureRoute(
    areaName: String,
    timeRemaining: String,
    deadlineTime: String,
    onEvent: (VerificationScreenEvent) -> Unit,
    onPhotoCaptured: (String) -> Unit,
) {
    val context = LocalContext.current
    var photoPath by rememberSaveable { mutableStateOf<String?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { captured ->
            val path = photoPath
            if (captured && path != null && File(path).isFile && File(path).length() > 0L) {
                val photo = File(path)
                if (photo.removeLocationMetadata() && photo.isValidCapture()) {
                    onPhotoCaptured(path)
                } else {
                    photo.delete()
                    photoPath = null
                    photoUri = null
                    android.widget.Toast
                        .makeText(context, R.string.verification_photo_invalid, android.widget.Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                path?.let(::File)?.delete()
                photoPath = null
                photoUri = null
            }
        }

    CameraCaptureScreen(areaName, timeRemaining, deadlineTime, { event ->
        if (event == VerificationScreenEvent.ShutterClick) {
            val directory = File(context.cacheDir, CAMERA_CAPTURE_DIRECTORY)
            if (directory.exists() || directory.mkdirs()) {
                val outputFile = File(directory, "${UUID.randomUUID()}.$JPEG_EXTENSION")
                photoPath = outputFile.absolutePath
                photoUri =
                    FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        outputFile,
                    )
                photoUri?.let(cameraLauncher::launch)
            }
        } else {
            onEvent(event)
        }
    })
}

private fun File.removeLocationMetadata(): Boolean =
    runCatching {
        ExifInterface(this).apply {
            GPS_METADATA_TAGS.forEach { setAttribute(it, null) }
            saveAttributes()
        }
    }.isSuccess

private fun File.isValidCapture(): Boolean {
    if (length() !in 1L..MAX_VERIFICATION_PHOTO_BYTES) return false
    val header = ByteArray(3)
    val hasJpegSignature =
        inputStream().use { stream ->
            stream.read(header) == header.size && header[0] == 0xFF.toByte() &&
                header[1] == 0xD8.toByte() && header[2] == 0xFF.toByte()
        }
    if (!hasJpegSignature) return false
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(absolutePath, options)
    return options.outWidth > 0 && options.outHeight > 0
}

private const val CAMERA_CAPTURE_DIRECTORY = "camera-captures"
private const val JPEG_EXTENSION = "jpg"
private const val MAX_VERIFICATION_PHOTO_BYTES = 10L * 1024L * 1024L
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
