package com.mte.marvelapp.ui.adapter.itemadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.data.remote.model.comic.Comic
import com.mte.marvelapp.databinding.RecyclerComicsLayoutBinding
import com.mte.marvelapp.ui.adapter.listener.ComicClickListener

class ComicsAdapter (private val comicClickListener: ComicClickListener) : PagingDataAdapter<Comic, ComicsAdapter.ComicsViewHolder>(
    diffUtil
) {

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<Comic>(){
            override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem == newItem
            }

        }
    }

    class ComicsViewHolder(private val binding : RecyclerComicsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comic: Comic,comicClickListener: ComicClickListener){
            binding.comic = comic
            binding.clickListener = comicClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {
        val binding = RecyclerComicsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ComicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem,comicClickListener)
        }
    }

}