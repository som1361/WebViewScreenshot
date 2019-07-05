package com.example.webviewscreenshot.DI.component

import com.example.webviewscreenshot.DI.module.AppModule
import com.example.webviewscreenshot.DI.Scope.AppScope
import com.example.webviewscreenshot.application.ScreenshotApplication
import com.example.webviewscreenshot.domain.model.ContentDao
import com.example.webviewscreenshot.view.HistoryActivity
import com.example.webviewscreenshot.view.MainActivity
import dagger.Component
import javax.inject.Singleton
@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {
fun inject(target:ScreenshotApplication)
    fun getContentDao(): ContentDao
}