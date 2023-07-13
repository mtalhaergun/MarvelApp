package com.mte.marvelapp.data.remote.model.character

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)