package com.example.webviewscreenshot.DI

import android.app.Application
import android.content.Context
import com.example.webviewscreenshot.domain.model.ContentDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app:Application) {

    @Provides
    @Singleton
    fun providesContext(): Context = app

    @Provides
    @Singleton
    fun providesContentDao(context: Context): ContentDao = ContentDao(context)
}