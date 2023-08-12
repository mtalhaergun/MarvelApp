package com.mte.marvelapp.domain.uimodel.creator

data class Events(
    val available: Int?,
    val collectionURI: String?,
    val items: List<Item>?,
    val returned: Int?
)