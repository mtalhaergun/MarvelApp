package com.mte.marvelapp.data.remote.model.stories

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    val name: String,
    val resourceURI: String
)