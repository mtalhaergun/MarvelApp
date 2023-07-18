package com.mte.marvelapp.data.remote.model.comic

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comic(
//    val characters: Characters,
//    val collectedIssues: List<CollectedIssue>,
//    val collections: List<Any>,
//    val creators: Creators,
//    val dates: List<Date>,
    @Json(name = "description")
    val description: String?,
//    val diamondCode: String,
//    val digitalId: Int,
//    val ean: String,
//    val events: Events,
//    val format: String,
    @Json(name = "id")
    val id: Int,
//    val images: List<Image>,
//    val isbn: String,
//    val issn: String,
//    val issueNumber: Int,
//    val modified: String,
//    val pageCount: Int,
//    val prices: List<Price>,
//    val resourceURI: String,
//    val series: Series,
//    val stories: Stories,
//    val textObjects: List<TextObject>,
    @Json(name = "thumbnail")
    val thumbnail: Thumbnail,
    @Json(name = "title")
    val title: String,
//    val upc: String,
//    val urls: List<Url>,
//    val variantDescription: String,
//    val variants: List<Variant>
)