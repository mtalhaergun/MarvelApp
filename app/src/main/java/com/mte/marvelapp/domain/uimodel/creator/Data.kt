package com.mte.marvelapp.domain.uimodel.creator

data class Data(
    val count: Int?,
    val limit: Int?,
    val offset: Int?,
    val creators: List<Creator>?,
    val total: Int?
)