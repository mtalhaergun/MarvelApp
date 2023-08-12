package com.mte.marvelapp.domain.uimodel.character

data class Comics(
    val available: Int?,
    val collectionURI: String?,
    val items: List<Item>?,
    val returned: Int?
)