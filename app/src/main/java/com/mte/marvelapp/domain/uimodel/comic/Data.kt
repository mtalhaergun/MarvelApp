package com.mte.marvelapp.domain.uimodel.comic

data class Data(
    val count: Int?,
    val limit: Int?,
    val offset: Int?,
    val comics: List<Comic>?,
    val total: Int?
)