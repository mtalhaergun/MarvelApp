package com.mte.marvelapp.ui.adapter.listener

import com.mte.marvelapp.data.remote.model.character.Character

interface CharacterClickListener {
    fun onCharacterClick(character : Character)
}