package com.mte.marvelapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.data.remote.model.event.EventsResponse
import com.mte.marvelapp.ui.home.HomeRepository
import com.mte.marvelapp.data.remote.service.NetworkResult
import com.mte.marvelapp.ui.details.DetailsRepository
import com.mte.marvelapp.utils.constants.Constants.PAGE_SIZE

class EventsPagingSource (private val repositoryHome: HomeRepository?,private val repositoryDetail : DetailsRepository?, private val id : String?) : PagingSource<Int, Events>() {

    constructor(repositoryHome: HomeRepository?) : this(repositoryHome,null,null)
    constructor(repositoryDetail: DetailsRepository?, id : String?) : this(null,repositoryDetail,id)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Events> {
        return try {

            val page = params.key ?: 0
            val offset = page * PAGE_SIZE

            val response: NetworkResult<EventsResponse>? = repositoryHome?.fetchEvents(offset)
                ?: repositoryDetail?.fetchCreatorsEvents(id.toString(), offset)

            when (response) {
                is NetworkResult.Success -> {
                    var events = response.data?.data?.events ?: emptyList()
                    val nextKey = if (offset >= response.data?.data?.total!!) null else page + 1
                    val prevKey = if (offset < 20) null else page - 1
                    LoadResult.Page(data = events, prevKey = prevKey, nextKey = nextKey)
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
        state: PagingState<Int, Events>
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