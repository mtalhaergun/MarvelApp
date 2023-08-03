package com.mte.marvelapp.data.remote.model.creator

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatorResponse(
    val attributionHTML: String?,
    val attributionText: String?,
    val code: Int?,
    val copyright: String?,
    @Json(name = "data")
    val `data`: Data,
    val etag: String?,
    val status: String?
)