package com.mte.marvelapp.data.remote.model.comic

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comic(
    @Json(name = "characters")
    val characters: Characters?,
    @Json(name = "collectedIssues")
    val collectedIssues: List<CollectedIssue>?,
    val collections: List<Any>?,
    @Json(name = "creators")
    val creators: Creators?,
    @Json(name = "dates")
    val dates: List<Date>?,
    @Json(name = "description")
    val description: String?,
    val diamondCode: String?,
    val digitalId: Int?,
    val ean: String?,
    @Json(name = "events")
    val events: Events?,
    val format: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "images")
    val images: List<Image>?,
    val isbn: String?,
    val issn: String?,
    val issueNumber: Int?,
    val modified: String?,
    val pageCount: Int?,
    @Json(name = "prices")
    val prices: List<Price>?,
    val resourceURI: String?,
    @Json(name = "series")
    val series: Series?,
    @Json(name = "stories")
    val stories: Stories?,
    @Json(name = "textObjects")
    val textObjects: List<TextObject>?,
    @Json(name = "thumbnail")
    val thumbnail: Thumbnail?,
    @Json(name = "title")
    val title: String?,
    val upc: String?,
    @Json(name = "urls")
    val urls: List<Url>?,
    val variantDescription: String?,
    @Json(name = "variants")
    val variants: List<Variant>?
)