package com.mte.marvelapp.ui.details

import com.mte.marvelapp.base.BaseRepository
import com.mte.marvelapp.data.remote.service.ApiService
import com.mte.marvelapp.utils.constants.Constants.API_KEY
import com.mte.marvelapp.utils.constants.Constants.HASH
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val apiService: ApiService) : BaseRepository() {

    suspend fun fetchCharactersSeries(id : String) = safeApiRequest {
        apiService.fetchCharactersSeries(id,"1", API_KEY, HASH)
    }

    suspend fun fetchCharacterDetail(id : String) = safeApiRequest {
        apiService.fetchCharacterDetail(id,"1", API_KEY, HASH)
    }
}