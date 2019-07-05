package com.example.webviewscreenshot.DI.component

import com.example.webviewscreenshot.DI.Scope.AsyncScope
import com.example.webviewscreenshot.DI.module.AsyncModule
import com.example.webviewscreenshot.domain.model.Content
import dagger.Component
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
@AsyncScope
@Component(modules = [AsyncModule::class])
interface AsyncComponent {

    @Named(AsyncModule.REMOVE_CONTENT_ERROR_OBSERVABLE)
    fun getRemoveContentErrorObservable(): PublishSubject<Exception>

    @Named(AsyncModule.SAVE_CONTENT_OBSERVABLE)
    fun getSaveContentObservable(): CompletableSubject

    @Named(AsyncModule.SAVE_CONTENT_ERROR_OBSERVABLE)
    fun getSaveContentErrorObservable(): PublishSubject<Exception>

    @Named(AsyncModule.REMOVE_CONTENT_OBSERVABLE)
    fun getRemoveContentObservable(): CompletableSubject

    @Named(AsyncModule.GET_CONTENT_OBSERVABLE)
    fun getGetContentObservable(): PublishSubject<ArrayList<Content>>

    @Named(AsyncModule.GET_CONTENT_ERROR_OBSERVABLE)
    fun getGetContentErrorObservable(): PublishSubject<Exception>

    @Named(AsyncModule.GET_CONTENT_BY_URL_OBSERVABLE)
    fun getGetContentByUrlObservable(): PublishSubject<ArrayList<Content>>

    @Named(AsyncModule.GET_CONTENT_BY_URL_ERROR_OBSERVABLE)
    fun getGetContentByUrlErrorObservable(): PublishSubject<Exception>
}