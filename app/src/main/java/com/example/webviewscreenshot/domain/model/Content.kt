package com.example.webviewscreenshot.domain.model

import java.time.LocalDateTime

data class Content(var imageRef: String?, var url: String?, var dateTime: String?){
    constructor() : this(null, null, null)
}
