package com.mte.marvelapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.data.remote.model.stories.Stories
import com.mte.marvelapp.databinding.RecyclerStoriesLayoutBinding
import com.mte.marvelapp.ui.adapter.listener.StoriesClickListener

class StoriesAdapter (private val storiesClickListener: StoriesClickListener) : PagingDataAdapter<Stories, StoriesAdapter.StoriesViewHolder>(
    diffUtil
){

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<Stories>(){
            override fun areItemsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem == newItem
            }

        }
    }

    class StoriesViewHolder (private val binding : RecyclerStoriesLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (stories: Stories,storiesClickListener: StoriesClickListener){
            binding.stories = stories
            binding.clickListener = storiesClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding = RecyclerStoriesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem,storiesClickListener)
        }
    }

}