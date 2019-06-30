package com.example.webviewscreenshot.domain.model

import android.content.Context

class Database(context: Context) {
    var contentDao = ContentDao(context)
    companion object {
        @Volatile
        private var instance: Database? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Database(context).also { instance = it }
        }
    }
}