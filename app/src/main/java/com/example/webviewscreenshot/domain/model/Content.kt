package com.example.webviewscreenshot.domain.model

import java.util.*

class Content {
    data class Content(var image_url: String?, var url: String?, var date: Date?){
        var id = 0
        constructor() : this(null, null, null)
    }
}