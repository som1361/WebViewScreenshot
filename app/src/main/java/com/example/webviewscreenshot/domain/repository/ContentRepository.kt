package com.example.webviewscreenshot.domain.repository

import com.example.webviewscreenshot.domain.model.Content
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ContentRepository {
    fun addContent(content: Content): Completable
    fun getContentList(): Single<List<Content>>
}