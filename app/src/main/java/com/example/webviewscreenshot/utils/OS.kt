package com.example.webviewscreenshot.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


 // Returns the absolute pathName to the saved Bitmap file
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

fun removeFromInternalStorage(filePath:String) {
    File(filePath).delete()
}

fun getCurrentTime(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

