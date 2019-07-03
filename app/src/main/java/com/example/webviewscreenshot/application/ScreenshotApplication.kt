package com.example.webviewscreenshot.application

import android.app.Application
import com.example.webviewscreenshot.DI.AppComponent
import com.example.webviewscreenshot.DI.AppModule
import com.example.webviewscreenshot.DI.DaggerAppComponent

class ScreenshotApplication : Application() {

    lateinit var screenShotComponent: AppComponent

    private fun initDagger(app: ScreenshotApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()

    override fun onCreate() {
        super.onCreate()
        screenShotComponent = initDagger(this)
    }
}