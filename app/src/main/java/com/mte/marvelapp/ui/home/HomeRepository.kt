package com.mte.marvelapp.ui.home

import com.mte.marvelapp.base.BaseRepository
import com.mte.marvelapp.data.remote.service.ApiService
import com.mte.marvelapp.utils.constants.Constants.API_KEY
import com.mte.marvelapp.utils.constants.Constants.HASH
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService) : BaseRepository() {

    suspend fun fetchCharacters() = safeApiRequest {
        apiService.fetchCharacters("1",API_KEY,HASH)
    }

}