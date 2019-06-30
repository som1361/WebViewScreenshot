package com.example.webviewscreenshot.domain.repository

import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.model.ContentDao
import io.reactivex.Completable
import io.reactivex.Single

class ContentDaoRepository(private val contentDao: ContentDao) : ContentRepository {
    override fun getContentList(): Single<List<Content>> = Single.fromCallable({contentDao.getContentList()})

    override fun addContent(content: Content): Completable = Completable.fromCallable { contentDao.addContent(content) }

    companion object {
        @Volatile
        private var instance: ContentDaoRepository? = null

        fun getInstance(contentDao: ContentDao) = instance ?: synchronized(this) {
            instance ?: ContentDaoRepository(contentDao).also { instance = it }
        }
    }
}