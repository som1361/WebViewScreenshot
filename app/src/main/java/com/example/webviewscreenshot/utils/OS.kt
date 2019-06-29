package com.example.webviewscreenshot.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.Gravity
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

fun showSuccessMessage(context: Context, message: Int) {
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    toast.view.setBackgroundColor(Color.GRAY)
    toast.setGravity(Gravity.BOTTOM, 0, 0);
    toast.show()
}

fun showFailMessage(context: Context, message: Int) {
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    toast.view.setBackgroundColor(Color.RED)
    toast.setGravity(Gravity.BOTTOM, 0, 0);
    toast.show()
}

/**
 * Returns the absolute pathName to the saved Bitmap file
 */
fun saveToInternalStorage(
    context: Context, bitmap: Bitmap, dirName: String,
    fileName: String
): String {
    // path to /data/data/yourapp/app_data/imageDir
    val directory = context.getDir(dirName, Context.MODE_PRIVATE)
    val pathName = File(directory, fileName)
    var fos= FileOutputStream(pathName)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    return pathName.getAbsolutePath()
}

