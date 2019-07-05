package com.example.webviewscreenshot.application

import android.app.Application
import com.example.webviewscreenshot.DI.component.AppComponent
import com.example.webviewscreenshot.DI.component.AsyncComponent
import com.example.webviewscreenshot.DI.component.DaggerAppComponent
import com.example.webviewscreenshot.DI.component.DaggerAsyncComponent
import com.example.webviewscreenshot.DI.module.AppModule

class ScreenshotApplication : Application() {

    lateinit var screenShotComponent: AppComponent
    companion object {
        private lateinit var  asyncComponent: AsyncComponent
        fun getAsyncComponent(): AsyncComponent{
            return asyncComponent
        }
    }

    private fun initDagger(app: ScreenshotApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()

    override fun onCreate() {
        super.onCreate()
        screenShotComponent = initDagger(this)
        asyncComponent = DaggerAsyncComponent.create()
    }
}