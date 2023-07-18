package com.mte.marvelapp.ui.details

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

    suspend fun fetchCharactersSeries(id : String) = safeApiRequest {
        apiService.fetchCharactersSeries(id,"1", API_KEY, HASH)
    }

    suspend fun fetchSeriesStories(id : String) = safeApiRequest {
        apiService.fetchSeriesStories(id,"1", API_KEY, HASH)
    }

    suspend fun fetchComicsCreators(id : String) = safeApiRequest {
        apiService.fetchComicsCreators(id,"1", API_KEY, HASH)
    }

    suspend fun fetchStoriesComics(id : String) = safeApiRequest {
        apiService.fetchStoriesComics(id,"1", API_KEY, HASH)
    }

    suspend fun fetchEventsCharacters(id : String) = safeApiRequest {
        apiService.fetchEventsCharacters(id,"1", API_KEY, HASH)
    }

    suspend fun fetchCreatorsComics(id : String) = safeApiRequest {
        apiService.fetchCreatorsComics(id,"1", API_KEY, HASH)
    }

}