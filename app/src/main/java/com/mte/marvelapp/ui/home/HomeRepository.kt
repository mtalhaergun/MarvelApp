package com.mte.marvelapp.ui.home

import com.mte.marvelapp.base.BaseRepository
import com.mte.marvelapp.data.remote.service.ApiService
import com.mte.marvelapp.utils.constants.Constants.API_KEY
import com.mte.marvelapp.utils.constants.Constants.HASH
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService) : BaseRepository() {

    suspend fun fetchCharacters(offset : Int) = safeApiRequest {
        apiService.fetchCharacters(offset,"1",API_KEY,HASH)
    }

    suspend fun fetchSeries(offset : Int) = safeApiRequest {
        apiService.fetchSeries(offset,"1",API_KEY,HASH)
    }

    suspend fun fetchComics(offset : Int) = safeApiRequest {
        apiService.fetchComics(offset,"1", API_KEY, HASH)
    }

    suspend fun fetchStories(offset : Int) = safeApiRequest {
        apiService.fetchStories(offset,"1", API_KEY, HASH)
    }

    suspend fun fetchEvents(offset : Int) = safeApiRequest {
        apiService.fetchEvents(offset,"1", API_KEY, HASH)
    }
}