package com.mte.marvelapp.domain.uimodel.series

data class SeriesResponse(
    val attributionHTML: String?,
    val attributionText: String?,
    val code: Int?,
    val copyright: String?,
    val `data`: Data,
    val etag: String?,
    val status: String?
)