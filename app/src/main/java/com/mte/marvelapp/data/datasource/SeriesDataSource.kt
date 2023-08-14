package com.mte.marvelapp.data.datasource

import com.mte.marvelapp.data.remote.model.series.SeriesResponse

interface SeriesDataSource {
    suspend fun fetchSeries() : SeriesResponse
}