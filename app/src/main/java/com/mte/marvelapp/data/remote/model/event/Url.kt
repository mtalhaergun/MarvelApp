package com.mte.marvelapp.data.remote.model.event

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Url(
    val type: String?,
    val url: String?
)