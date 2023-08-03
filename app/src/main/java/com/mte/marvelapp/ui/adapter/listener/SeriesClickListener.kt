package com.mte.marvelapp.ui.adapter.listener

import com.mte.marvelapp.data.remote.model.series.Series

interface SeriesClickListener {
    fun onSeriesClick(series : Series)
}