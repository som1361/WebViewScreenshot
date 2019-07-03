package com.example.webviewscreenshot.DI

import android.app.Application
import android.content.Context
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
    @Singleton
    fun providesContext(): Context = app

    @Provides
    @Singleton
    fun providesContentDao(context: Context): ContentDao = ContentDao(context)

    @Provides
    @Named(MainViewModel.GET_CONTENT_ERROR_OBSERVABLE)
    fun provideGetContentErrorObservable(): PublishSubject<Exception> = PublishSubject.create()

    @Provides
    @Named(MainViewModel.GET_CONTENT_BY_URL_ERROR_OBSERVABLE)
    fun provideGetContentByUrlErrorObservable(): PublishSubject<Exception> = PublishSubject.create()

    @Provides
    @Named(MainViewModel.SAVE_CONTENT_OBSERVABLE)
    fun provideSaveContentObservable(): CompletableSubject = CompletableSubject.create()

    @Provides
    @Named(MainViewModel.REMOVE_CONTENT_OBSERVABLE)
    fun provideRemoveContentObservable(): CompletableSubject = CompletableSubject.create()


    @Provides
    @Named(MainViewModel.SAVE_CONTENT_ERROR_OBSERVABLE)
    fun provideSaveContentErrorObservable(): PublishSubject<Exception> = PublishSubject.create()


    @Provides
    @Named(MainViewModel.REMOVE_CONTENT_ERROR_OBSERVABLE)
    fun provideRemoveContentErrorObservable(): PublishSubject<Exception> = PublishSubject.create()

    @Provides
    @Named(MainViewModel.GET_CONTENT_OBSERVABLE)
    fun provideGetContentObservable(): PublishSubject<ArrayList<Content>> = PublishSubject.create()

    @Provides
    @Named(MainViewModel.GET_CONTENT_BY_URL_OBSERVABLE)
    fun provideGetContentByUrlObservable(): PublishSubject<ArrayList<Content>> = PublishSubject.create()
}