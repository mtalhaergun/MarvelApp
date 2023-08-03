package com.mte.marvelapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding
import com.mte.marvelapp.ui.adapter.listener.SeeAllClickListener

class ComicsRecyclerAdapter (private val comicsAdapter: ComicsAdapter,
                             private val seeAllClickListener: SeeAllClickListener
) : RecyclerView.Adapter<ComicsRecyclerAdapter.ComicsRecyclerViewHolder>() {

    private var shimmerVisibility = true

    class ComicsRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val comicsAdapter: ComicsAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seeAllClickListener: SeeAllClickListener, shimmerVisibility : Boolean){
            binding.rvCategories.adapter = comicsAdapter
            binding.categoryTitle.text = "Comics"
            binding.seeAllTitle.setOnClickListener {
                seeAllClickListener.onSeeAllClick("comics")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ComicsRecyclerViewHolder(binding,comicsAdapter)
    }

    override fun onBindViewHolder(holder: ComicsRecyclerViewHolder, position: Int) {
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