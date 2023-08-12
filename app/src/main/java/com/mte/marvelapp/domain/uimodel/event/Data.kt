package com.mte.marvelapp.domain.uimodel.event

data class Data(
    val count: Int?,
    val limit: Int?,
    val offset: Int?,
    val events: List<Events>?,
    val total: Int?
)