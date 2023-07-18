package com.mte.marvelapp.ui.home.uistate

import com.mte.marvelapp.data.remote.model.creator.Creator

sealed class CreatorsUiState {
    object Loading : CreatorsUiState()
    data class Success(val data: List<Creator>?) : CreatorsUiState()
    data class Error(val message: String?) : CreatorsUiState()
}