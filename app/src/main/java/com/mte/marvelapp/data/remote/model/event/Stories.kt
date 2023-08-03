package com.mte.marvelapp.data.remote.model.event

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stories(
    val available: Int?,
    val collectionURI: String?,
    @Json(name = "items")
    val items: List<ItemXXXX>?,
    val returned: Int?
)