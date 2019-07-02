package com.example.webviewscreenshot.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.Toast

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