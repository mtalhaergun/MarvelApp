package com.mte.marvelapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.data.remote.model.series.Series
import com.mte.marvelapp.databinding.RecyclerSeriesLayoutBinding
import com.mte.marvelapp.ui.adapter.listener.SeriesClickListener

class SeriesAdapter(private val seriesClickListener: SeriesClickListener) : PagingDataAdapter<Series, SeriesAdapter.SeriesViewHolder>(
    diffUtil
) {

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<Series>(){
            override fun areItemsTheSame(oldItem: Series, newItem: Series): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Series, newItem: Series): Boolean {
                return oldItem == newItem
            }

        }
    }

    class SeriesViewHolder (private val binding : RecyclerSeriesLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(series : Series, seriesClickListener: SeriesClickListener){
            binding.series = series
            binding.clickListener = seriesClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val binding = RecyclerSeriesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem,seriesClickListener)
        }
    }

}