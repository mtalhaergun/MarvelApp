package com.mte.marvelapp.domain.uimodel.stories

data class StoriesResponse(
    val attributionHTML: String?,
    val attributionText: String?,
    val code: Int?,
    val copyright: String?,
    val `data`: Data,
    val etag: String?,
    val status: String?
)