package com.mte.marvelapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mte.marvelapp.data.remote.model.character.CharacterResponse
import com.mte.marvelapp.data.remote.model.comic.ComicResponse
import com.mte.marvelapp.data.remote.model.creator.CreatorResponse
import com.mte.marvelapp.data.remote.model.event.EventsResponse
import com.mte.marvelapp.data.remote.model.series.SeriesResponse
import com.mte.marvelapp.data.remote.model.stories.StoriesResponse
import com.mte.marvelapp.data.remote.service.NetworkResult
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.ui.home.uistate.ComicsUiState
import com.mte.marvelapp.ui.home.uistate.CreatorsUiState
import com.mte.marvelapp.ui.home.uistate.EventsUiState
import com.mte.marvelapp.ui.home.uistate.SeriesUiState
import com.mte.marvelapp.ui.home.uistate.StoriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository : DetailsRepository) : ViewModel() {

    private val _characterUiState = MutableLiveData<CharacterUiState>()
    val characterUiState: LiveData<CharacterUiState> = _characterUiState

    private val _seriesUiState = MutableLiveData<SeriesUiState>()
    val seriesUiState: LiveData<SeriesUiState> = _seriesUiState

    private val _comicsUiState = MutableLiveData<ComicsUiState>()
    val comicsUiState: LiveData<ComicsUiState> = _comicsUiState

    private val _storiesUiState = MutableLiveData<StoriesUiState>()
    val storiesUiState: LiveData<StoriesUiState> = _storiesUiState

    private val _eventsUiState = MutableLiveData<EventsUiState>()
    val eventsUiState: LiveData<EventsUiState> = _eventsUiState

    private val _creatorsUiState = MutableLiveData<CreatorsUiState>()
    val creatorsUiState: LiveData<CreatorsUiState> = _creatorsUiState

    val characterResponse : MutableLiveData<CharacterResponse?> = MutableLiveData()
    val seriesResponse : MutableLiveData<SeriesResponse?> = MutableLiveData()
    val comicResponse : MutableLiveData<ComicResponse?> = MutableLiveData()
    val storiesResponse : MutableLiveData<StoriesResponse?> = MutableLiveData()
    val eventResponse : MutableLiveData<EventsResponse?> = MutableLiveData()
    val creatorResponse : MutableLiveData<CreatorResponse?> = MutableLiveData()

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

    fun fetchSeriesDetail(id : String) = viewModelScope.launch {
        val request = repository.fetchSeriesDetail(id)
        when(request){
            is NetworkResult.Success -> {
                seriesResponse.value = request.data
            }
            is NetworkResult.Error -> {

            }
        }
    }

    fun fetchComicDetail(id : String) = viewModelScope.launch {
        val request = repository.fetchComicDetail(id)
        when(request){
            is NetworkResult.Success -> {
                comicResponse.value = request.data
            }
            is NetworkResult.Error -> {

            }
        }
    }

    fun fetchStoriesDetail(id : String) = viewModelScope.launch {
        val request = repository.fetchStoriesDetail(id)
        when(request){
            is NetworkResult.Success -> {
                storiesResponse.value = request.data
            }
            is NetworkResult.Error -> {

            }
        }
    }

    fun fetchEventDetail(id : String) = viewModelScope.launch {
        val request = repository.fetchEventDetail(id)
        when(request){
            is NetworkResult.Success -> {
                eventResponse.value = request.data
            }
            is NetworkResult.Error -> {

            }
        }
    }

    fun fetchCreatorDetail(id : String) = viewModelScope.launch {
        val request = repository.fetchCreatorDetail(id)
        when(request){
            is NetworkResult.Success -> {
                creatorResponse.value = request.data
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

    fun fetchSeriesStories(id : String) = viewModelScope.launch {
        _storiesUiState.value = StoriesUiState.Loading

        val request = repository.fetchSeriesStories(id)
        when(request){
            is NetworkResult.Success -> {
                _storiesUiState.value = StoriesUiState.Success(request.data?.data?.stories)
            }
            is NetworkResult.Error -> {
                _storiesUiState.value = StoriesUiState.Error(request.message)
            }
        }
    }

    fun fetchComicsCreators(id : String) = viewModelScope.launch {
        _creatorsUiState.value = CreatorsUiState.Loading

        val request = repository.fetchComicsCreators(id)
        when(request){
            is NetworkResult.Success -> {
                _creatorsUiState.value = CreatorsUiState.Success(request.data?.data?.creators)
            }
            is NetworkResult.Error -> {
                _creatorsUiState.value = CreatorsUiState.Error(request.message)
            }
        }
    }

    fun fetchStoriesComics(id : String) = viewModelScope.launch {
        _comicsUiState.value = ComicsUiState.Loading

        val request = repository.fetchStoriesComics(id)
        when(request){
            is NetworkResult.Success -> {
                _comicsUiState.value = ComicsUiState.Success(request.data?.data?.comics)
            }
            is NetworkResult.Error -> {
                _comicsUiState.value = ComicsUiState.Error(request.message)
            }
        }
    }

    fun fetchEventsCharacters(id : String) = viewModelScope.launch {
        _characterUiState.value = CharacterUiState.Loading

        val request = repository.fetchEventsCharacters(id)
        when(request){
            is NetworkResult.Success -> {
                _characterUiState.value = CharacterUiState.Success(request.data?.data?.characters)
            }
            is NetworkResult.Error -> {
                _characterUiState.value = CharacterUiState.Error(request.message)
            }
        }
    }

    fun fetchCreatorsComics(id : String) = viewModelScope.launch {
        _comicsUiState.value = ComicsUiState.Loading

        val request = repository.fetchCreatorsComics(id)
        when(request){
            is NetworkResult.Success -> {
                _comicsUiState.value = ComicsUiState.Success(request.data?.data?.comics)
            }
            is NetworkResult.Error -> {
                _comicsUiState.value = ComicsUiState.Error(request.message)
            }
        }
    }
}