package com.example.webviewscreenshot.DI.module

import android.app.Application
import android.content.Context
import com.example.webviewscreenshot.DI.Scope.AppScope
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.model.ContentDao
import com.example.webviewscreenshot.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val app:Application) {

    @Provides
    @AppScope
    fun providesContext(): Context = app

    @Provides
    @AppScope
    fun providesContentDao(context: Context): ContentDao = ContentDao(context)
}