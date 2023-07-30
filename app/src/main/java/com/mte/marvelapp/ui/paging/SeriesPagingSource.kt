package com.mte.marvelapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.data.remote.model.series.SeriesResponse
import com.mte.marvelapp.ui.home.HomeRepository
import com.mte.marvelapp.data.remote.service.NetworkResult
import com.mte.marvelapp.ui.details.DetailsRepository
import com.mte.marvelapp.ui.seeall.SeeAllRepository
import com.mte.marvelapp.utils.constants.Constants.PAGE_SIZE

class SeriesPagingSource (private val repositoryHome: HomeRepository?,
                          private val repositoryDetail : DetailsRepository?,
                          private val repositorySeeAll : SeeAllRepository?,
                          private val id : String?) : PagingSource<Int, Series>() {

    constructor(repositoryHome: HomeRepository?) : this(repositoryHome,null,null,null)
    constructor(repositoryDetail: DetailsRepository?, id : String?) : this(null,repositoryDetail,null,id)
    constructor(repositorySeeAll: SeeAllRepository?, id : String?) : this(null,null,repositorySeeAll,id)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Series> {
        return try {

            val page = params.key ?: 0
            val offset = page * PAGE_SIZE

            val response: NetworkResult<SeriesResponse>? = repositoryHome?.fetchSeries(offset)
                ?: repositoryDetail?.fetchCharactersSeries(id.toString(), offset) ?: repositorySeeAll?.searchSeries(id!!,offset)

            when (response) {
                is NetworkResult.Success -> {
                    var series = response.data?.data?.series ?: emptyList()
                    val nextKey = if (offset >= response.data?.data?.total!!) null else page + 1
                    val prevKey = if (offset < 20) null else page - 1
                    LoadResult.Page(data = series, prevKey = prevKey, nextKey = nextKey)
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
        state: PagingState<Int, Series>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(
                anchorPosition
            )
            anchorPage?.prevKey?.plus(1) ?:
            anchorPage?.nextKey?.minus(1)
        }
    }
}