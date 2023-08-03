package com.mte.marvelapp.data.remote.model.comic

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Series(
    val name: String?,
    val resourceURI: String?
)