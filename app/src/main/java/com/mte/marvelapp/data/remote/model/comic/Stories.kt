package com.mte.marvelapp.data.remote.model.comic

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stories(
    val available: Int?,
    val collectionURI: String?,
    @Json(name = "items")
    val items: List<ItemXX>?,
    val returned: Int?
)