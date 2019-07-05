package com.example.webviewscreenshot.DI.module

import android.app.Activity
import android.content.Context
import com.example.webviewscreenshot.DI.Scope.ActivityScope
import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.model.ContentDao
import com.example.webviewscreenshot.domain.repository.ContentDaoRepository
import com.example.webviewscreenshot.domain.repository.ContentRepository
import com.example.webviewscreenshot.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Named
import javax.inject.Singleton

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @ActivityScope
    fun providesContext(): Context = activity

    /////ViewModel////////////////////////////////////
    @Provides
    @ActivityScope
    fun providesViewModel(contentRepository: ContentRepository) = MainViewModel(contentRepository)

    /////Model////////////////////////////////////////
    @Provides
    @ActivityScope
    fun providesContentRepository(contentDao: ContentDao): ContentRepository = ContentDaoRepository(contentDao)

}
