package com.mte.marvelapp.data.remote.service

import com.mte.marvelapp.data.remote.model.character.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("characters")
    suspend fun fetchCharacters(
        @Query("ts") ts: Int,
        @Query("apikey") apiKey: String,
        @Query("hash") hash : String
    ) : CharacterResponse

}