package com.mte.marvelapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mte.marvelapp.data.repository.HomeRepository
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.character.CharacterResponse
import com.mte.marvelapp.data.remote.service.NetworkResult
import com.mte.marvelapp.data.repository.DetailsRepository
import com.mte.marvelapp.data.repository.SeeAllRepository
import com.mte.marvelapp.utils.constants.Constants.PAGE_SIZE

class CharactersPagingSource (private val repositoryHome: HomeRepository? = null,
                              private val repositoryDetail: DetailsRepository? = null,
                              private val repositorySeeAll: SeeAllRepository? = null,
                              private val id: String? = null
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {

            val page = params.key ?: 0
            val offset = page * PAGE_SIZE

            val response: NetworkResult<CharacterResponse>? = fetchCharacters(offset)

            when (response) {
                is NetworkResult.Success -> {
                    var characters = response.data?.data?.characters ?: emptyList()
                    val nextKey = if (offset >= response.data?.data?.total!!) null else page + 1
                    val prevKey = if (offset < 20) null else page - 1
                    LoadResult.Page(data = characters, prevKey = prevKey, nextKey = nextKey)
                }
                else -> {
                    LoadResult.Error(Exception(response?.message))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, Character>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(
                anchorPosition
            )
            anchorPage?.prevKey?.plus(1) ?:
            anchorPage?.nextKey?.minus(1)
        }
    }

    private suspend fun fetchCharacters(offset: Int): NetworkResult<CharacterResponse>? {
        return repositoryHome?.fetchCharacters(offset)
            ?: repositoryDetail?.fetchEventsCharacters(id.toString(), offset)
            ?: repositorySeeAll?.searchCharacters(id!!, offset)
    }
}