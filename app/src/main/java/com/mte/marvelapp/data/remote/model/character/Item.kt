package com.mte.marvelapp.data.remote.model.character

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "name")
    val name: String?,
    @Json(name = "resourceURI")
    val resourceURI: String?
)