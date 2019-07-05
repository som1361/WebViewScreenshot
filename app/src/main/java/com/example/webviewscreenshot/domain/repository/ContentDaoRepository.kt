package com.example.webviewscreenshot.domain.repository

import com.example.webviewscreenshot.domain.model.Content
import com.example.webviewscreenshot.domain.model.ContentDao
import io.reactivex.Completable
import io.reactivex.Single
import java.util.ArrayList
import javax.inject.Inject

class ContentDaoRepository @Inject constructor(private val contentDao: ContentDao) : ContentRepository {
    override fun getContentsByUrl(url: String): Single<ArrayList<Content>> = Single.fromCallable({contentDao.getContentsByUrl(url)})

    override fun getContentList(): Single<ArrayList<Content>> = Single.fromCallable({contentDao.getContentList()})

    override fun addContent(content: Content): Completable = Completable.fromCallable { contentDao.addContent(content) }

    override fun removeContent(content: Content): Completable = Completable.fromCallable { contentDao.removeContent(content) }
}