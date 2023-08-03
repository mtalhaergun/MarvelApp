package com.mte.marvelapp.data.remote.model.creator

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Url(
    val type: String?,
    val url: String?
)