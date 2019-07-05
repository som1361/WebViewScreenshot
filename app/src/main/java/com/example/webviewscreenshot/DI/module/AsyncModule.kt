package com.example.webviewscreenshot.DI.module

import com.example.webviewscreenshot.DI.Scope.AsyncScope
import com.example.webviewscreenshot.domain.model.Content
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

@Module
class AsyncModule() {
    companion object {
        const val SAVE_CONTENT_OBSERVABLE = "saveContentObservable"
        const val REMOVE_CONTENT_OBSERVABLE = "removeContentObservable"
        const val SAVE_CONTENT_ERROR_OBSERVABLE = "saveContentErrorObservable"
        const val GET_CONTENT_ERROR_OBSERVABLE = "getContentErrorObservable"
        const val GET_CONTENT_BY_URL_ERROR_OBSERVABLE = "getContentByUrlErrorObservable"
        const val REMOVE_CONTENT_ERROR_OBSERVABLE = "removeContentErrorObservable"
        const val GET_CONTENT_OBSERVABLE = "getContentObservable"
        const val GET_CONTENT_BY_URL_OBSERVABLE = "getContentByUrlObservable"
    }

    @Provides
    @AsyncScope
    @Named(GET_CONTENT_ERROR_OBSERVABLE)
    fun provideGetContentErrorObservable(): PublishSubject<Exception> = PublishSubject.create()

    @Provides
    @AsyncScope
    @Named(GET_CONTENT_BY_URL_ERROR_OBSERVABLE)
    fun provideGetContentByUrlErrorObservable(): PublishSubject<Exception> = PublishSubject.create()

    @Provides
    @AsyncScope
    @Named(SAVE_CONTENT_OBSERVABLE)
    fun provideSaveContentObservable(): CompletableSubject = CompletableSubject.create()

    @Provides
    @AsyncScope
    @Named(REMOVE_CONTENT_OBSERVABLE)
    fun provideRemoveContentObservable(): CompletableSubject = CompletableSubject.create()


    @Provides
    @AsyncScope
    @Named(SAVE_CONTENT_ERROR_OBSERVABLE)
    fun provideSaveContentErrorObservable(): PublishSubject<Exception> = PublishSubject.create()


    @Provides
    @AsyncScope
    @Named(REMOVE_CONTENT_ERROR_OBSERVABLE)
    fun provideRemoveContentErrorObservable(): PublishSubject<Exception> = PublishSubject.create()

    @Provides
    @AsyncScope
    @Named(GET_CONTENT_OBSERVABLE)
    fun provideGetContentObservable(): PublishSubject<ArrayList<Content>> = PublishSubject.create()

    @Provides
    @AsyncScope
    @Named(GET_CONTENT_BY_URL_OBSERVABLE)
    fun provideGetContentByUrlObservable(): PublishSubject<ArrayList<Content>> = PublishSubject.create()
}