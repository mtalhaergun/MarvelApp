package com.mte.marvelapp.data.datasource

import com.mte.marvelapp.data.remote.model.stories.StoriesResponse

interface StoriesDataSource {
    suspend fun fetchStories() : StoriesResponse
}