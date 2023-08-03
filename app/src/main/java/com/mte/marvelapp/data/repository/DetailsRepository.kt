package com.mte.marvelapp.data.repository

import com.mte.marvelapp.base.BaseRepository
import com.mte.marvelapp.data.remote.service.ApiService
import com.mte.marvelapp.utils.constants.Constants.API_KEY
import com.mte.marvelapp.utils.constants.Constants.HASH
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val apiService: ApiService) : BaseRepository() {

    suspend fun fetchCharacterDetail(id : String) = safeApiRequest {
        apiService.fetchCharacterDetail(id,"1", API_KEY, HASH)
    }

    suspend fun fetchSeriesDetail(id : String) = safeApiRequest {
        apiService.fetchSeriesDetail(id,"1", API_KEY, HASH)
    }

    suspend fun fetchComicDetail(id : String) = safeApiRequest {
        apiService.fetchComicDetail(id,"1", API_KEY, HASH)
    }

    suspend fun fetchStoriesDetail(id : String) = safeApiRequest {
        apiService.fetchStoriesDetail(id,"1", API_KEY, HASH)
    }

    suspend fun fetchEventDetail(id : String) = safeApiRequest {
        apiService.fetchEventDetail(id,"1", API_KEY, HASH)
    }

    suspend fun fetchCreatorDetail(id : String) = safeApiRequest {
        apiService.fetchCreatorDetail(id,"1", API_KEY, HASH)
    }

    suspend fun fetchCharactersSeries(id : String, offset : Int) = safeApiRequest {
        apiService.fetchCharactersSeries(id,offset,"1", API_KEY, HASH)
    }

    suspend fun fetchSeriesStories(id : String, offset : Int) = safeApiRequest {
        apiService.fetchSeriesStories(id,offset,"1", API_KEY, HASH)
    }

    suspend fun fetchComicsCreators(id : String, offset : Int) = safeApiRequest {
        apiService.fetchComicsCreators(id,offset,"1", API_KEY, HASH)
    }

    suspend fun fetchStoriesComics(id : String, offset : Int) = safeApiRequest {
        apiService.fetchStoriesComics(id,offset,"1", API_KEY, HASH)
    }

    suspend fun fetchEventsCharacters(id : String, offset : Int) = safeApiRequest {
        apiService.fetchEventsCharacters(id,offset,"1", API_KEY, HASH)
    }

    suspend fun fetchCreatorsEvents(id : String, offset : Int) = safeApiRequest {
        apiService.fetchCreatorsEvents(id,offset,"1", API_KEY, HASH)
    }

}