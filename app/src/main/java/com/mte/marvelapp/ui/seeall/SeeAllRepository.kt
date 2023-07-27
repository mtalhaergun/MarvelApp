package com.mte.marvelapp.ui.seeall

import com.mte.marvelapp.base.BaseRepository
import com.mte.marvelapp.data.remote.service.ApiService
import com.mte.marvelapp.utils.constants.Constants.API_KEY
import com.mte.marvelapp.utils.constants.Constants.HASH
import javax.inject.Inject

class SeeAllRepository @Inject constructor(private val apiService: ApiService) : BaseRepository() {

    suspend fun searchCharacters(name : String, offset : Int) = safeApiRequest {
        apiService.searchCharacters(offset,name,"1",API_KEY,HASH)
    }

    suspend fun fetchCharacters() = safeApiRequest {
        apiService.fetchCharacters(0,"1",API_KEY,HASH)
    }
}