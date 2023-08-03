package com.mte.marvelapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding
import com.mte.marvelapp.ui.adapter.listener.SeeAllClickListener

class SeriesRecyclerAdapter (private val seriesAdapter: SeriesAdapter,
                             private val seeAllClickListener: SeeAllClickListener
) : RecyclerView.Adapter<SeriesRecyclerAdapter.SeriesRecyclerViewHolder>() {

    private var shimmerVisibility = true

    class SeriesRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val seriesAdapter: SeriesAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seeAllClickListener: SeeAllClickListener, shimmerVisibility : Boolean){
            binding.rvCategories.adapter = seriesAdapter
            binding.categoryTitle.text = "Series"
            binding.seeAllTitle.setOnClickListener {
                seeAllClickListener.onSeeAllClick("series")
            }
            if(shimmerVisibility){
                binding.recyclerListLayout.visibility = View.GONE
                binding.recyclerShimmerInclude.root.visibility = View.VISIBLE
                binding.recyclerShimmerInclude.shimmerRecyclerLayout.startShimmer()
            }else{
                binding.recyclerListLayout.visibility = View.VISIBLE
                binding.recyclerShimmerInclude.root.visibility = View.GONE
                binding.recyclerShimmerInclude.shimmerRecyclerLayout.stopShimmer()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SeriesRecyclerViewHolder(binding,seriesAdapter)
    }

    override fun onBindViewHolder(holder: SeriesRecyclerViewHolder, position: Int) {
        holder.bind(seeAllClickListener,shimmerVisibility)
    }

    override fun getItemCount(): Int = 1

    fun stopShimmer(){
        if(shimmerVisibility){
            shimmerVisibility = false
            notifyDataSetChanged()
        }
    }
}