package com.mte.marvelapp.data.remote.model.creator

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    val name: String?,
    val resourceURI: String?
)