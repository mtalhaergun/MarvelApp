package com.mte.marvelapp.data.remote.model.stories

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OriginalIssue(
    val name: String?,
    val resourceURI: String?
)