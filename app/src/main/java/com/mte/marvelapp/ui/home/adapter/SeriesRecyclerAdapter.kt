package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding

class SeriesRecyclerAdapter (private val seriesAdapter: SeriesAdapter) : RecyclerView.Adapter<SeriesRecyclerAdapter.SeriesRecyclerViewHolder>() {

    class SeriesRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val seriesAdapter: SeriesAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.rvCategories.adapter = seriesAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SeriesRecyclerViewHolder(binding,seriesAdapter)
    }

    override fun onBindViewHolder(holder: SeriesRecyclerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}