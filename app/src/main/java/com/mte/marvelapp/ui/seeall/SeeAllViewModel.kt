package com.mte.marvelapp.ui.seeall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.mte.marvelapp.ui.home.HomeRepository
import com.mte.marvelapp.ui.paging.CharactersPagingSource
import com.mte.marvelapp.ui.paging.ComicsPagingSource
import com.mte.marvelapp.ui.paging.EventsPagingSource
import com.mte.marvelapp.ui.paging.SeriesPagingSource
import com.mte.marvelapp.utils.constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeeAllViewModel @Inject constructor(private val repository: SeeAllRepository, private val repositoryHome: HomeRepository) : ViewModel() {

    private val _characters = MutableStateFlow<PagingData<Character>?>(null)
    val characters: Flow<PagingData<Character>?> = _characters.asStateFlow()

    private var charactersPagingData : PagingData<Character>? = null
    private var charactersSearchPagingData : PagingData<Character>? = null

    private val _series = MutableStateFlow<PagingData<Series>?>(null)
    val series: Flow<PagingData<Series>?> = _series.asStateFlow()

    private var seriesPagingData : PagingData<Series>? = null
    private var seriesSearchPagingData : PagingData<Series>? = null

    private val _comics = MutableStateFlow<PagingData<Comic>?>(null)
    val comics: Flow<PagingData<Comic>?> = _comics.asStateFlow()

    private var comicsPagingData : PagingData<Comic>? = null
    private var comicsSearchPagingData : PagingData<Comic>? = null

    private val _events = MutableStateFlow<PagingData<Events>?>(null)
    val events: Flow<PagingData<Events>?> = _events.asStateFlow()

    private var eventsPagingData : PagingData<Events>? = null
    private var eventsSearchPagingData : PagingData<Events>? = null

    fun searchCharacters(name : String?) = viewModelScope.launch{
        charactersSearchPagingData = Pager(
            PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                prefetchDistance = 1
            )
        ) {
            CharactersPagingSource(repository,name)
        }.flow.cachedIn(viewModelScope).first()

        _characters.value = charactersSearchPagingData
    }

    fun fetchCharacters() = viewModelScope.launch {
        charactersPagingData = Pager(PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            prefetchDistance = 1
        )) {
            CharactersPagingSource(repositoryHome)
        }.flow.cachedIn(viewModelScope).first()

        _characters.value = charactersPagingData
    }

    fun searchSeries(title : String?) = viewModelScope.launch{
        seriesSearchPagingData = Pager(
            PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                prefetchDistance = 1
            )
        ) {
            SeriesPagingSource(repository,title)
        }.flow.cachedIn(viewModelScope).first()

        _series.value = seriesSearchPagingData
    }

    fun fetchSeries() = viewModelScope.launch {
        seriesPagingData = Pager(PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            prefetchDistance = 1
        )) {
            SeriesPagingSource(repositoryHome)
        }.flow.cachedIn(viewModelScope).first()

        _series.value = seriesPagingData
    }

    fun searchComics(title : String?) = viewModelScope.launch{
        comicsSearchPagingData = Pager(
            PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                prefetchDistance = 1
            )
        ) {
            ComicsPagingSource(repository,title)
        }.flow.cachedIn(viewModelScope).first()

        _comics.value = comicsSearchPagingData
    }

    fun fetchComics() = viewModelScope.launch {
        comicsPagingData = Pager(PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            prefetchDistance = 1
        )) {
            ComicsPagingSource(repositoryHome)
        }.flow.cachedIn(viewModelScope).first()

        _comics.value = comicsPagingData
    }

    fun searchEvents(name : String?) = viewModelScope.launch{
        eventsSearchPagingData = Pager(
            PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                prefetchDistance = 1
            )
        ) {
            EventsPagingSource(repository,name)
        }.flow.cachedIn(viewModelScope).first()

        _events.value = eventsSearchPagingData
    }

    fun fetchEvents() = viewModelScope.launch {
        eventsPagingData = Pager(PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            prefetchDistance = 1
        )) {
            EventsPagingSource(repositoryHome)
        }.flow.cachedIn(viewModelScope).first()

        _events.value = eventsPagingData
    }
}