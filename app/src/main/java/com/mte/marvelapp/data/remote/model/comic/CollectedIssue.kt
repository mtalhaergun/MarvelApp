package com.mte.marvelapp.data.remote.model.comic

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectedIssue(
    val name: String?,
    val resourceURI: String?
)