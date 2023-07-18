package com.mte.marvelapp.data.remote.model.event

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemXX(
    val name: String,
    val resourceURI: String,
    val role: String
)