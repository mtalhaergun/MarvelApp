package com.mte.marvelapp.data.remote.model.creator

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Creator(
//    val comics: Comics,
//    val events: Events,
//    val firstName: String,
    val fullName: String,
    val id: Int,
//    val lastName: String,
//    val middleName: String,
//    val modified: String,
//    val resourceURI: String,
//    val series: Series,
//    val stories: Stories,
//    val suffix: String,
    @Json(name = "thumbnail")
    val thumbnail: Thumbnail,
//    val urls: List<Url>
)