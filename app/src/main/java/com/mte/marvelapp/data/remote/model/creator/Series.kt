package com.mte.marvelapp.data.remote.model.creator

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)