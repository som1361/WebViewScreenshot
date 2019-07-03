package com.example.webviewscreenshot.DI

import com.example.webviewscreenshot.domain.model.ContentDao
import com.example.webviewscreenshot.domain.repository.ContentDaoRepository
import com.example.webviewscreenshot.domain.repository.ContentRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun providesContentRepository(contentDao: ContentDao): ContentRepository = ContentDaoRepository(contentDao)
}