package com.mte.marvelapp.domain.uimodel.event

data class Characters(
    val available: Int?,
    val collectionURI: String?,
    val items: List<Item>?,
    val returned: Int?
)