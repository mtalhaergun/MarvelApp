package com.mte.marvelapp.ui.adapter.listener

import com.mte.marvelapp.data.remote.model.comic.Comic

interface ComicClickListener {
    fun onComicClick(comic : Comic)
}