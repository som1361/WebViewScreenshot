package com.example.webviewscreenshot.domain.repository

import com.example.webviewscreenshot.domain.model.Content
import io.reactivex.Completable
import io.reactivex.Observable

interface ContentRepository {
    fun addContent(content: Content): Completable
}