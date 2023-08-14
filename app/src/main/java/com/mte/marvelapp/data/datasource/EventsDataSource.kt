package com.mte.marvelapp.data.datasource

import com.mte.marvelapp.data.remote.model.event.EventsResponse

interface EventsDataSource {
    suspend fun fetchEvents() : EventsResponse
}