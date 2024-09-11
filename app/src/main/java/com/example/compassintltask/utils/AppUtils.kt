package com.example.compassintltask.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.ByteArrayOutputStream


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    val result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.createImageUri(): Uri {
    val imagesDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File.createTempFile(
        "IMG_", ".jpg", imagesDir
    )
    val packageName = this.applicationContext.packageName
    return FileProvider.getUriForFile(
        this, "$packageName.provider", imageFile
    )
}

fun Context.saveCaptureImageAndGetPath(uri: Uri): String {
    val bitmap = BitmapFactory.decodeStream(this.contentResolver.openInputStream(uri))
    val maxWidth = 1024
    val maxHeight = 1024
    val quality = 80

    val compressedBitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true)
    val compressedStream = ByteArrayOutputStream()
    compressedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, compressedStream)
    var compressedBytes = compressedStream.toByteArray()

    if (compressedBytes.size > 1024 * 1024) {
        // Image is still larger than 1 MB, try reducing quality further
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, quality - 10, compressedStream)
        compressedBytes = compressedStream.toByteArray()
    }

    val compressedFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "compressed_image.jpg")
    FileOutputStream(compressedFile).use { compressedOutputStream ->
        compressedOutputStream.write(compressedBytes)
    }
    return compressedFile.absolutePath
}

fun Context.getActualFilePath(uri:Uri): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = this.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return it.getString(columnIndex)
        }
    }
    return ""
}

fun Context.showErrorDialog(message: String) {
    AlertDialog.Builder(this)
        .setTitle("Error")
        .setMessage(message)
        .setPositiveButton("OK", null)
        .show()

}
