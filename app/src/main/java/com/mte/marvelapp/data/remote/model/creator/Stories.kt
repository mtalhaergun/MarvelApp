package com.mte.marvelapp.data.remote.model.creator

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stories(
    val available: Int,
    val collectionURI: String,
    @Json(name = "items")
    val items: List<ItemXXX>,
    val returned: Int
)