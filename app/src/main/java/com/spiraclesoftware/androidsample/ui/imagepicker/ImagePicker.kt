package com.spiraclesoftware.androidsample.ui.imagepicker

import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spiraclesoftware.androidsample.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.File

class ImagePicker(private val fragment: Fragment) {

    private val context = fragment.requireContext()

    fun showDialog(onImagePicked: (Uri) -> Unit) =
        MaterialAlertDialogBuilder(context)
            .setMessage(R.string.image_picker__message)
            .setNegativeButton(R.string.image_picker__take_picture) { _, _ ->
                takePictureWithCamera { imageUri ->
                    onImagePicked(imageUri)
                }
            }
            .setPositiveButton(R.string.image_picker__from_gallery) { _, _ ->
                pickImageFromGallery { imageUri ->
                    onImagePicked(imageUri)
                }
            }.show()!!

    private fun pickImageFromGallery(onPicked: (Uri) -> Unit) {
        val contract = ActivityResultContracts.GetContent()
        val launcher = fragment.registerForActivityResult(contract) { uri ->
            if (uri != null) onPicked(uri)
        }

        launcher.launch("image/*")
    }

    private fun takePictureWithCamera(onPictureTaken: (Uri) -> Unit) {
        val authority = context.packageName + ".provider"
        val imageUri = FileProvider.getUriForFile(context, authority, createImageFile())

        val contract = ActivityResultContracts.TakePicture()
        val launcher = fragment.registerForActivityResult(contract) { savedSuccessfully ->
            if (savedSuccessfully)
                onPictureTaken(imageUri)
        }

        launcher.launch(imageUri)
    }

    private fun createImageFile(): File {
        val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val extension = ".jpg"

        return File.createTempFile(timeStamp, extension, directory)
    }

}