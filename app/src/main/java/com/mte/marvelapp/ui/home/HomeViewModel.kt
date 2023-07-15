package com.mte.marvelapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mte.marvelapp.data.remote.service.NetworkResult
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.ui.home.uistate.SeriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository : HomeRepository) : ViewModel() {

    private val _characterUiState = MutableLiveData<CharacterUiState>()
    val characterUiState: LiveData<CharacterUiState> = _characterUiState

    private val _seriesUiState = MutableLiveData<SeriesUiState>()
    val seriesUiState: LiveData<SeriesUiState> = _seriesUiState

    fun fetchCharacters() = viewModelScope.launch {
        _characterUiState.value = CharacterUiState.Loading

        val request = repository.fetchCharacters()
        when(request){
            is NetworkResult.Success -> {
                _characterUiState.value = CharacterUiState.Success(request.data?.data?.characters)
            }
            is NetworkResult.Error -> {
                _characterUiState.value = CharacterUiState.Error(request.message)
            }
        }
    }

    fun fetchSeries() = viewModelScope.launch {
        _seriesUiState.value = SeriesUiState.Loading

        val request = repository.fetchSeries()
        when(request){
            is NetworkResult.Success -> {
                _seriesUiState.value = SeriesUiState.Success(request.data?.data?.series)
            }
            is NetworkResult.Error -> {
                _seriesUiState.value = SeriesUiState.Error(request.message)
            }
        }
    }

}