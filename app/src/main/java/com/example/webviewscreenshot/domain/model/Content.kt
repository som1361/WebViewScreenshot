package com.example.webviewscreenshot.domain.model

data class Content(var image_url: String?, var url: String?) {
    var id = 0

    constructor() : this(null, null)
}
