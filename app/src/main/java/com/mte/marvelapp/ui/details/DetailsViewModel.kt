package com.mte.marvelapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mte.marvelapp.data.remote.model.character.CharacterResponse
import com.mte.marvelapp.data.remote.service.NetworkResult
import com.mte.marvelapp.ui.home.uistate.SeriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository : DetailsRepository) : ViewModel() {

    private val _seriesUiState = MutableLiveData<SeriesUiState>()
    val seriesUiState: LiveData<SeriesUiState> = _seriesUiState

    val characterResponse : MutableLiveData<CharacterResponse?> = MutableLiveData()

    fun fetchCharacterDetail(id : String) = viewModelScope.launch {
        val request = repository.fetchCharacterDetail(id)
        when(request){
            is NetworkResult.Success -> {
                characterResponse.value = request.data
            }
            is NetworkResult.Error -> {

            }
        }
    }

    fun fetchCharactersSeries(id : String) = viewModelScope.launch {
        _seriesUiState.value = SeriesUiState.Loading

        val request = repository.fetchCharactersSeries(id)
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