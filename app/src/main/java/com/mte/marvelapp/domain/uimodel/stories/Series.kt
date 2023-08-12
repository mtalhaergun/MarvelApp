package com.mte.marvelapp.domain.uimodel.stories

data class Series(
    val available: Int?,
    val collectionURI: String?,
    val items: List<Item>?,
    val returned: Int?
)