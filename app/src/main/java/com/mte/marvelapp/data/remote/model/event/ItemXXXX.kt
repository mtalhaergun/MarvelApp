package com.mte.marvelapp.data.remote.model.event

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemXXXX(
    val name: String,
    val resourceURI: String,
    val type: String
)