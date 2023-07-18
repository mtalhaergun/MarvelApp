package com.mte.marvelapp.data.remote.model.creator

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Thumbnail(
    val extension: String,
    val path: String
)