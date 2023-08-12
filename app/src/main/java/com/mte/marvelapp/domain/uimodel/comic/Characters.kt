package com.mte.marvelapp.domain.uimodel.comic

data class Characters(
    val available: Int?,
    val collectionURI: String?,
    val items: List<Item>?,
    val returned: Int?
)