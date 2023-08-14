package com.mte.marvelapp.data.datasource

import com.mte.marvelapp.data.remote.model.character.CharacterResponse

interface CharactersDataSource {
    suspend fun fetchCharacters() : CharacterResponse
}