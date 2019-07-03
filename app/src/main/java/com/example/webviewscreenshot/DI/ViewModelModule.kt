package com.example.webviewscreenshot.DI

import com.example.webviewscreenshot.domain.repository.ContentRepository
import com.example.webviewscreenshot.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun providesViewModel(contentRepository: ContentRepository) = MainViewModel(contentRepository)
}