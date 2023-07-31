package com.mte.marvelapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.character.CharacterResponse
import com.mte.marvelapp.data.remote.model.comic.Comic
import com.mte.marvelapp.data.remote.model.comic.ComicResponse
import com.mte.marvelapp.data.remote.model.creator.Creator
import com.mte.marvelapp.data.remote.model.creator.CreatorResponse
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.data.remote.model.event.EventsResponse
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.data.remote.model.series.SeriesResponse
import com.mte.marvelapp.data.remote.model.stories.Stories
import com.mte.marvelapp.data.remote.model.stories.StoriesResponse
import com.mte.marvelapp.data.remote.service.NetworkResult
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.ui.home.uistate.ComicsUiState
import com.mte.marvelapp.ui.home.uistate.CreatorsUiState
import com.mte.marvelapp.ui.home.uistate.EventsUiState
import com.mte.marvelapp.ui.home.uistate.SeriesUiState
import com.mte.marvelapp.ui.home.uistate.StoriesUiState
import com.mte.marvelapp.ui.paging.CharactersPagingSource
import com.mte.marvelapp.ui.paging.ComicsPagingSource
import com.mte.marvelapp.ui.paging.CreatorsPagingSource
import com.mte.marvelapp.ui.paging.EventsPagingSource
import com.mte.marvelapp.ui.paging.SeriesPagingSource
import com.mte.marvelapp.ui.paging.StoriesPagingSource
import com.mte.marvelapp.utils.constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository : DetailsRepository) : ViewModel() {

    private val _characterUiState = MutableLiveData<CharacterUiState>()
    val characterUiState: LiveData<CharacterUiState> = _characterUiState

    private val _characters = MutableStateFlow<PagingData<Character>?>(null)
    val characters: Flow<PagingData<Character>?> = _characters.asStateFlow()

    private var charactersPagingData : PagingData<Character>? = null

    private val _seriesUiState = MutableLiveData<SeriesUiState>()
    val seriesUiState: LiveData<SeriesUiState> = _seriesUiState

    private val _series = MutableStateFlow<PagingData<Series>?>(null)
    val series: Flow<PagingData<Series>?> = _series.asStateFlow()

    private var seriesPagingData : PagingData<Series>? = null

    private val _comicsUiState = MutableLiveData<ComicsUiState>()
    val comicsUiState: LiveData<ComicsUiState> = _comicsUiState

    private val _comics = MutableStateFlow<PagingData<Comic>?>(null)
    val comics: Flow<PagingData<Comic>?> = _comics.asStateFlow()

    private var comicsPagingData : PagingData<Comic>? = null

    private val _storiesUiState = MutableLiveData<StoriesUiState>()
    val storiesUiState: LiveData<StoriesUiState> = _storiesUiState

    private val _stories = MutableStateFlow<PagingData<Stories>?>(null)
    val stories: Flow<PagingData<Stories>?> = _stories.asStateFlow()

    private var storiesPagingData : PagingData<Stories>? = null

    private val _eventsUiState = MutableLiveData<EventsUiState>()
    val eventsUiState: LiveData<EventsUiState> = _eventsUiState

    private val _events = MutableStateFlow<PagingData<Events>?>(null)
    val events: Flow<PagingData<Events>?> = _events.asStateFlow()

    private var eventsPagingData : PagingData<Events>? = null

    private val _creatorsUiState = MutableLiveData<CreatorsUiState>()
    val creatorsUiState: LiveData<CreatorsUiState> = _creatorsUiState

    private val _creators = MutableStateFlow<PagingData<Creator>?>(null)
    val creators: Flow<PagingData<Creator>?> = _creators.asStateFlow()

    private var creatorsPagingData : PagingData<Creator>? = null

    val characterResponse : MutableLiveData<CharacterResponse?> = MutableLiveData()
    val seriesResponse : MutableLiveData<SeriesResponse?> = MutableLiveData()
    val comicResponse : MutableLiveData<ComicResponse?> = MutableLiveData()
    val storiesResponse : MutableLiveData<StoriesResponse?> = MutableLiveData()
    val eventResponse : MutableLiveData<EventsResponse?> = MutableLiveData()
    val creatorResponse : MutableLiveData<CreatorResponse?> = MutableLiveData()

    fun fetchCharacterDetail(id : String) = viewModelScope.launch {
        characterResponse.value = null
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
        seriesResponse.value = null
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
        comicResponse.value = null
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
        storiesResponse.value = null
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
        eventResponse.value = null
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
        creatorResponse.value = null
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

        try {
            if(seriesPagingData == null){
                seriesPagingData = Pager(
                    PagingConfig(
                        pageSize = Constants.PAGE_SIZE,
                        prefetchDistance = 1
                    )
                ) {
                    SeriesPagingSource(repository,id)
                }.flow.cachedIn(viewModelScope).first()

                _series.value = seriesPagingData
            }else{
                _series.value = seriesPagingData
            }

            _seriesUiState.value = SeriesUiState.Success(emptyList<Series>())
        } catch (e: Exception) {
            _seriesUiState.value = SeriesUiState.Error(e.message)
        }
    }

    fun fetchSeriesStories(id : String) = viewModelScope.launch {
        _storiesUiState.value = StoriesUiState.Loading

        try {
            if(storiesPagingData == null){
                storiesPagingData = Pager(PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    prefetchDistance = 1
                )) {
                    StoriesPagingSource(repository,id)
                }.flow.cachedIn(viewModelScope).first()

                _stories.value = storiesPagingData
            }else{
                _stories.value = storiesPagingData
            }

            _storiesUiState.value = StoriesUiState.Success(emptyList<Stories>())
        } catch (e: Exception) {
            _storiesUiState.value = StoriesUiState.Error(e.message)
        }
    }

    fun fetchComicsCreators(id : String) = viewModelScope.launch {
        _creatorsUiState.value = CreatorsUiState.Loading

        try {
            if(creatorsPagingData == null){
                creatorsPagingData = Pager(PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    prefetchDistance = 1
                )) {
                    CreatorsPagingSource(repository,id)
                }.flow.cachedIn(viewModelScope).first()

                _creators.value = creatorsPagingData
            }else{
                _creators.value = creatorsPagingData
            }

            _creatorsUiState.value = CreatorsUiState.Success(emptyList<Creator>())
        } catch (e: Exception) {
            _creatorsUiState.value = CreatorsUiState.Error(e.message)
        }
    }

    fun fetchStoriesComics(id : String) = viewModelScope.launch {
        _comicsUiState.value = ComicsUiState.Loading

        try {
            if(comicsPagingData == null){
                comicsPagingData = Pager(PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    prefetchDistance = 1
                )) {
                    ComicsPagingSource(repository,id)
                }.flow.cachedIn(viewModelScope).first()

                _comics.value = comicsPagingData
            }else{
                _comics.value = comicsPagingData
            }

            _comicsUiState.value = ComicsUiState.Success(emptyList<Comic>())
        } catch (e: Exception) {
            _comicsUiState.value = ComicsUiState.Error(e.message)
        }
    }

    fun fetchEventsCharacters(id : String) = viewModelScope.launch {
        _characterUiState.value = CharacterUiState.Loading

        try {
            if(charactersPagingData == null){
                charactersPagingData = Pager(
                    PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    prefetchDistance = 1
                )
                ) {
                    CharactersPagingSource(repository,id)
                }.flow.cachedIn(viewModelScope).first()

                _characters.value = charactersPagingData
            }else{
                _characters.value = charactersPagingData
            }

            _characterUiState.value = CharacterUiState.Success(emptyList<Character>())
        } catch (e: Exception) {
            _characterUiState.value = CharacterUiState.Error(e.message)
        }
    }

    fun fetchCreatorsEvents(id : String) = viewModelScope.launch {
        _eventsUiState.value = EventsUiState.Loading

        try {
            if(eventsPagingData == null){
                eventsPagingData = Pager(PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    prefetchDistance = 1
                )) {
                    EventsPagingSource(repository,id)
                }.flow.cachedIn(viewModelScope).first()

                _events.value = eventsPagingData
            }else{
                _events.value = eventsPagingData
            }

            _eventsUiState.value = EventsUiState.Success(emptyList<Events>())
        } catch (e: Exception) {
            _eventsUiState.value = EventsUiState.Error(e.message)
        }
    }
}