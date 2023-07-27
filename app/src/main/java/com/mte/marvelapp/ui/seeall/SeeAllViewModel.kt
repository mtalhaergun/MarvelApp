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
import com.mte.marvelapp.ui.home.HomeRepository
import com.mte.marvelapp.ui.home.uistate.CharacterUiState
import com.mte.marvelapp.ui.paging.CharactersPagingSource
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

    private val _characterUiState = MutableLiveData<CharacterUiState>()
    val characterUiState: LiveData<CharacterUiState> = _characterUiState

    private val _characters = MutableStateFlow<PagingData<Character>?>(null)
    val characters: Flow<PagingData<Character>?> = _characters.asStateFlow()

    private var charactersPagingData : PagingData<Character>? = null
    private var charactersSearchPagingData : PagingData<Character>? = null

    fun searchCharacters(name : String?) = viewModelScope.launch{
        _characterUiState.value = CharacterUiState.Loading

        try {
//            if(charactersSearchPagingData == null){
                charactersSearchPagingData = Pager(
                    PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    prefetchDistance = 1
                )
                ) {
                    CharactersPagingSource(repository,name)
                }.flow.cachedIn(viewModelScope).first()

                _characters.value = charactersSearchPagingData
//            }else{
//                _characters.value = charactersSearchPagingData
//            }

            _characterUiState.value = CharacterUiState.Success(emptyList<Character>())
        } catch (e: Exception) {
            _characterUiState.value = CharacterUiState.Error(e.message)
        }

    }

    fun fetchCharacters() = viewModelScope.launch {
        _characterUiState.value = CharacterUiState.Loading

        try {
            if(charactersPagingData == null){
                charactersPagingData = Pager(PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    prefetchDistance = 1
                )) {
                    CharactersPagingSource(repositoryHome)
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
}