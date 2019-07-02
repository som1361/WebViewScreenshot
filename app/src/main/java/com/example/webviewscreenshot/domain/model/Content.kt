package com.example.webviewscreenshot.domain.model

import java.sql.Timestamp
import java.time.LocalDateTime

data class Content(var imageRef: String?, var url: String?, var timestamp: String?){
    constructor() : this(null, null, null)
}
