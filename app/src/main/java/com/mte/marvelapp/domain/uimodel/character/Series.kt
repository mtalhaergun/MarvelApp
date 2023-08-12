package com.mte.marvelapp.domain.uimodel.character

data class Series(
    val available: Int?,
    val collectionURI: String?,
    val items: List<Item>?,
    val returned: Int?
)