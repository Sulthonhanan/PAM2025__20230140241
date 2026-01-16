package com.example.medicord.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object ImagePicker {
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    fun copyFileToInternalStorage(context: Context, sourceUri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(sourceUri)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "patient_photo_$timeStamp.jpg"
            val file = File(context.filesDir, fileName)
            
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFileProviderUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}

data class ImagePickerLaunchers(
    val cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>,
    val galleryLauncher: androidx.activity.result.ActivityResultLauncher<String>,
    val cameraPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>,
    val storagePermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>,
    val createCameraFile: () -> Uri?,
    val hasCameraPermission: () -> Boolean,
    val hasStoragePermission: () -> Boolean
)

@Composable
fun rememberImagePickerLauncher(
    onImageSelected: (String?) -> Unit
): ImagePickerLaunchers {
    val context = LocalContext.current
    var cameraFile by remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraFile?.absolutePath?.let { path ->
                onImageSelected(path)
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val path = ImagePicker.copyFileToInternalStorage(context, it)
            onImageSelected(path)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createCameraFile(context) { file -> cameraFile = file }
            uri?.let { cameraLauncher.launch(it) }
        }
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        }
    }

    fun createCameraFile(): Uri? {
        val file = ImagePicker.createImageFile(context)
        cameraFile = file
        return ImagePicker.getFileProviderUri(context, file)
    }

    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    return remember {
        ImagePickerLaunchers(
            cameraLauncher = cameraLauncher,
            galleryLauncher = galleryLauncher,
            cameraPermissionLauncher = cameraPermissionLauncher,
            storagePermissionLauncher = storagePermissionLauncher,
            createCameraFile = ::createCameraFile,
            hasCameraPermission = ::hasCameraPermission,
            hasStoragePermission = ::hasStoragePermission
        )
    }
}

private fun createCameraFile(context: Context, onFileCreated: (File) -> Unit): Uri? {
    val file = ImagePicker.createImageFile(context)
    onFileCreated(file)
    return ImagePicker.getFileProviderUri(context, file)
}
