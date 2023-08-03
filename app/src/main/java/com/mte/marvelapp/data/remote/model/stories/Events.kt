package com.mte.marvelapp.data.remote.model.stories

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Events(
    val available: Int?,
    val collectionURI: String?,
    @Json(name = "items")
    val items: List<Any>?,
    val returned: Int?
)