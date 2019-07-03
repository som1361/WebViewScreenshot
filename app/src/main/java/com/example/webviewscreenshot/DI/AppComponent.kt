package com.example.webviewscreenshot.DI

import com.example.webviewscreenshot.DI.Scope.AppScope
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.view.HistoryActivity
import com.example.webviewscreenshot.view.MainActivity
import com.example.webviewscreenshot.viewmodel.MainViewModel
import dagger.Component
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@AppScope
@Component(modules = [AppModule::class, RepositoryModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: HistoryActivity)

    @Named(MainViewModel.REMOVE_CONTENT_ERROR_OBSERVABLE)
    fun getRemoveContentErrorObservable(): PublishSubject<Exception>

    @Named(MainViewModel.SAVE_CONTENT_OBSERVABLE)
    fun getSaveContentObservable(): CompletableSubject

    @Named(MainViewModel.SAVE_CONTENT_ERROR_OBSERVABLE)
    fun getSaveContentErrorObservable(): PublishSubject<Exception>

    @Named(MainViewModel.REMOVE_CONTENT_OBSERVABLE)
    fun getRemoveContentObservable(): CompletableSubject

    @Named(MainViewModel.GET_CONTENT_OBSERVABLE)
    fun getGetContentObservable(): PublishSubject<ArrayList<Content>>

    @Named(MainViewModel.GET_CONTENT_ERROR_OBSERVABLE)
    fun getGetContentErrorObservable(): PublishSubject<Exception>

    @Named(MainViewModel.GET_CONTENT_BY_URL_OBSERVABLE)
    fun getGetContentByUrlObservable(): PublishSubject<ArrayList<Content>>

    @Named(MainViewModel.GET_CONTENT_BY_URL_ERROR_OBSERVABLE)
   fun getGetContentByUrlErrorObservable(): PublishSubject<Exception>

}