package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding

class StoriesRecyclerAdapter (private val storiesAdapter: StoriesAdapter) : RecyclerView.Adapter<StoriesRecyclerAdapter.StoriesRecyclerViewHolder>() {

    class StoriesRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val storiesAdapter: StoriesAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.rvCategories.adapter = storiesAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoriesRecyclerViewHolder(binding,storiesAdapter)
    }

    override fun onBindViewHolder(holder: StoriesRecyclerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}