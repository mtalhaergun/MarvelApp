package com.mte.marvelapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.comic.Comic
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.data.remote.model.stories.Stories
import com.mte.marvelapp.data.repository.HomeRepository
import com.mte.marvelapp.ui.paging.CharactersPagingSource
import com.mte.marvelapp.ui.paging.ComicsPagingSource
import com.mte.marvelapp.ui.paging.EventsPagingSource
import com.mte.marvelapp.ui.paging.SeriesPagingSource
import com.mte.marvelapp.ui.paging.StoriesPagingSource
import com.mte.marvelapp.utils.constants.Constants.PAGE_SIZE
import com.mte.marvelapp.utils.constants.Constants.PREFETCH_DISTANCE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository : HomeRepository) : ViewModel() {

    private val _characters = MutableStateFlow<PagingData<Character>?>(null)
    val characters: Flow<PagingData<Character>?> = _characters.asStateFlow()

    private var charactersPagingData : PagingData<Character>? = null

    private val _series = MutableStateFlow<PagingData<Series>?>(null)
    val series: Flow<PagingData<Series>?> = _series.asStateFlow()

    private var seriesPagingData : PagingData<Series>? = null

    private val _comics = MutableStateFlow<PagingData<Comic>?>(null)
    val comics: Flow<PagingData<Comic>?> = _comics.asStateFlow()

    private var comicsPagingData : PagingData<Comic>? = null

    private val _stories = MutableStateFlow<PagingData<Stories>?>(null)
    val stories: Flow<PagingData<Stories>?> = _stories.asStateFlow()

    private var storiesPagingData : PagingData<Stories>? = null

    private val _events = MutableStateFlow<PagingData<Events>?>(null)
    val events: Flow<PagingData<Events>?> = _events.asStateFlow()

    private var eventsPagingData : PagingData<Events>? = null

    fun fetchCharacters() = viewModelScope.launch {
            charactersPagingData = Pager(PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            )) {
                CharactersPagingSource(repository)
            }.flow.cachedIn(viewModelScope).first()
        _characters.value = charactersPagingData
    }

    fun fetchSeries() = viewModelScope.launch {
            seriesPagingData = Pager(PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            )) {
                SeriesPagingSource(repository)
            }.flow.cachedIn(viewModelScope).first()
        _series.value = seriesPagingData
    }

    fun fetchComics() = viewModelScope.launch {
            comicsPagingData = Pager(PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            )) {
                ComicsPagingSource(repository)
            }.flow.cachedIn(viewModelScope).first()
        _comics.value = comicsPagingData
    }

    fun fetchStories() = viewModelScope.launch {
            storiesPagingData = Pager(PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            )) {
                StoriesPagingSource(repository)
            }.flow.cachedIn(viewModelScope).first()
        _stories.value = storiesPagingData
    }

    fun fetchEvents() = viewModelScope.launch {
            eventsPagingData = Pager(PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            )) {
                EventsPagingSource(repository)
            }.flow.cachedIn(viewModelScope).first()
        _events.value = eventsPagingData
    }

}