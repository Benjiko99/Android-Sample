package com.spiraclesoftware.androidsample.components.image_picker

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spiraclesoftware.androidsample.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import androidx.activity.result.contract.ActivityResultContracts.GetContent as GetContentContract
import androidx.activity.result.contract.ActivityResultContracts.TakePicture as TakePictureContract

/**
 * Let's the user take a picture with the camera or pick an image from the gallery.
 *
 * Result listeners have to be registered on the calling [Fragment] with registerResultListeners().
 * This can only be done before the [Fragment] has been initialized, e.g. in onCreate().
 */
class ImagePicker {

    private var onResultAction: ((Uri) -> Unit)? = null
    private var cameraFileUri: Uri? = null

    private var galleryLauncher: ActivityResultLauncher<String>? = null
    private var cameraLauncher: ActivityResultLauncher<Uri>? = null

    /** Has to be called in onCreate() of the calling [fragment]. */
    fun registerResultListeners(fragment: Fragment) {
        galleryLauncher = fragment.registerForActivityResult(GetContentContract()) { uri ->
            if (uri != null)
                onResultAction?.invoke(uri)
        }

        cameraLauncher = fragment.registerForActivityResult(TakePictureContract()) { savedSuccessfully ->
            if (savedSuccessfully)
                onResultAction?.invoke(cameraFileUri!!)
        }
    }

    fun showDialog(context: Context, onResult: (Uri) -> Unit) =
        MaterialAlertDialogBuilder(context)
            .setMessage(R.string.image_picker__message)
            .setNegativeButton(R.string.image_picker__take_picture) { _, _ ->
                onResultAction = onResult
                takePictureWithCamera(context)
            }
            .setPositiveButton(R.string.image_picker__from_gallery) { _, _ ->
                onResultAction = onResult
                pickImageFromGallery()
            }
            .setOnCancelListener {
                onResultAction = null
            }.show()!!

    private fun pickImageFromGallery() {
        galleryLauncher?.launch("image/*")
    }

    private fun takePictureWithCamera(context: Context) {
        val authority = context.packageName + ".provider"
        cameraFileUri = FileProvider.getUriForFile(context, authority, createCameraFile(context))

        cameraLauncher?.launch(cameraFileUri)
    }

    private fun createCameraFile(context: Context): File {
        val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val extension = ".jpg"

        return File.createTempFile(timeStamp, extension, directory)
    }

}