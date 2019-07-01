package com.example.webviewscreenshot.domain.repository

import com.example.webviewscreenshot.domain.model.Content
import io.reactivex.Completable
import io.reactivex.Single
import java.util.ArrayList

interface ContentRepository {
    fun addContent(content: Content): Completable
    fun getContentList(): Single<ArrayList<Content>>
    fun removeContent(content: Content): Completable
}