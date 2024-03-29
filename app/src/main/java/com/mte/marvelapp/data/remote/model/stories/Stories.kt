package com.mte.marvelapp.data.remote.model.stories

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stories(
    @Json(name = "characters")
    val characters: Characters?,
    @Json(name = "comics")
    val comics: Comics?,
    @Json(name = "creators")
    val creators: Creators?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "events")
    val events: Events?,
    @Json(name = "id")
    val id: Int?,
    val modified: String?,
    @Json(name = "originalIssue")
    val originalIssue: OriginalIssue?,
    val resourceURI: String?,
    @Json(name = "series")
    val series: Series?,
    @Json(name = "thumbnail")
    val thumbnail: Thumbnail?,
    @Json(name = "title")
    val title: String?,
    val type: String?
)