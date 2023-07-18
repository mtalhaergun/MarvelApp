package com.mte.marvelapp.data.remote.model.creator

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    @Json(name = "results")
    val creators: List<Creator>,
    val total: Int
)