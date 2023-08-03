package com.mte.marvelapp.data.remote.model.event

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Events(
    @Json(name = "characters")
    val characters: Characters?,
    @Json(name = "comics")
    val comics: Comics?,
    @Json(name = "creators")
    val creators: Creators?,
    val description: String?,
    val end: String?,
    @Json(name = "id")
    val id: Int?,
    val modified: String?,
    @Json(name = "next")
    val next: Next?,
    @Json(name = "previous")
    val previous: Previous?,
    val resourceURI: String?,
    @Json(name = "series")
    val series: Series?,
    val start: String?,
    @Json(name = "stories")
    val stories: Stories?,
    @Json(name = "thumbnail")
    val thumbnail: Thumbnail?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "urls")
    val urls: List<Url>?
)