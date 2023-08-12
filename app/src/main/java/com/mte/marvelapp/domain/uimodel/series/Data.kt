package com.mte.marvelapp.domain.uimodel.series

data class Data(
    val count: Int?,
    val limit: Int?,
    val offset: Int?,
    val series: List<Series>?,
    val total: Int?
)