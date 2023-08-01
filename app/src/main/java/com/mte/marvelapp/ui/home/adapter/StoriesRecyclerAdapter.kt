package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding
import com.mte.marvelapp.ui.home.adapter.listener.SeeAllClickListener

class StoriesRecyclerAdapter (private val storiesAdapter: StoriesAdapter,
                              private val seeAllClickListener: SeeAllClickListener) : RecyclerView.Adapter<StoriesRecyclerAdapter.StoriesRecyclerViewHolder>() {

    private var shimmerVisibility = true

    class StoriesRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val storiesAdapter: StoriesAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seeAllClickListener: SeeAllClickListener,shimmerVisibility : Boolean){
            binding.rvCategories.adapter = storiesAdapter
            binding.categoryTitle.text = "Stories"
            binding.seeAllTitle.setOnClickListener {
                seeAllClickListener.onSeeAllClick("stories")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoriesRecyclerViewHolder(binding,storiesAdapter)
    }

    override fun onBindViewHolder(holder: StoriesRecyclerViewHolder, position: Int) {
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