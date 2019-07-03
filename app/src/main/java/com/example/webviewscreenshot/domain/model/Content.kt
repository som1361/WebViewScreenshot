package com.example.webviewscreenshot.domain.model

data class Content(var imageRef: String?, var url: String?, var timestamp: String?){
    constructor() : this(null, null, null)
}
