package com.mte.marvelapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mte.marvelapp.data.remote.model.comic.Comic
import com.mte.marvelapp.data.remote.model.comic.ComicResponse
import com.mte.marvelapp.data.repository.HomeRepository
import com.mte.marvelapp.data.remote.service.NetworkResult
import com.mte.marvelapp.data.repository.DetailsRepository
import com.mte.marvelapp.data.repository.SeeAllRepository
import com.mte.marvelapp.utils.constants.Constants.PAGE_SIZE

class ComicsPagingSource (private val repositoryHome: HomeRepository? = null,
                          private val repositoryDetail: DetailsRepository? = null,
                          private val repositorySeeAll: SeeAllRepository? = null,
                          private val id: String? = null
) : PagingSource<Int, Comic>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comic> {
        return try {

            val page = params.key ?: 0
            val offset = page * PAGE_SIZE

            val response: NetworkResult<ComicResponse>? = fetchComics(offset)

            when (response) {
                is NetworkResult.Success -> {
                    var comics = response.data?.data?.comics ?: emptyList()
                    val nextKey = if (offset >= response.data?.data?.total!!) null else page + 1
                    val prevKey = if (offset < 20) null else page - 1
                    LoadResult.Page(data = comics, prevKey = prevKey, nextKey = nextKey)
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
        state: PagingState<Int, Comic>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(
                anchorPosition
            )
            anchorPage?.prevKey?.plus(1) ?:
            anchorPage?.nextKey?.minus(1)
        }
    }

    private suspend fun fetchComics(offset: Int): NetworkResult<ComicResponse>? {
        return repositoryHome?.fetchComics(offset)
            ?: repositoryDetail?.fetchStoriesComics(id.toString(), offset)
            ?: repositorySeeAll?.searchComics(id!!, offset)
    }
}