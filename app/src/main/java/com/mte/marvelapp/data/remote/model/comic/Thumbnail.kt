package com.mte.marvelapp.data.remote.model.comic

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Thumbnail(
    @Json(name = "extension")
    val extension: String?,
    @Json(name = "path")
    val path: String?
)