package com.mte.marvelapp.domain.uimodel.stories

data class Data(
    val count: Int?,
    val limit: Int?,
    val offset: Int?,
    val stories: List<Stories>?,
    val total: Int?
)