package com.example.webviewscreenshot.utils

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.util.PatternsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.R.layout
import android.graphics.Canvas


fun String.isValidUrl(): Boolean
        = this.isNotEmpty() &&
        PatternsCompat.WEB_URL.matcher(this).matches()

fun AppCompatActivity.hideSoftKeyboard(){
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun ProgressBar.show(){
    this.visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    this.visibility = View.GONE
}

fun View.loadBitmap(): Bitmap {
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    this.layout(0, 0, this.layoutParams.width, layoutParams.height)
    draw(c)
    return b
}
