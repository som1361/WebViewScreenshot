package com.example.webviewscreenshot.viewmodel

import com.example.webviewscreenshot.domain.repository.HistoryRepository

class MainViewModel() {
    private lateinit var screenshotRepository: HistoryRepository
    constructor(mScreenshotRepository: HistoryRepository) : this() {
        screenshotRepository = mScreenshotRepository
    }
}