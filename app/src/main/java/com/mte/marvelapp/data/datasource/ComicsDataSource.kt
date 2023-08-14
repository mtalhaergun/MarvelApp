package com.mte.marvelapp.data.datasource

import com.mte.marvelapp.data.remote.model.comic.ComicResponse

interface ComicsDataSource {
    suspend fun fetchComics() : ComicResponse
}