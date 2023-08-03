package com.mte.marvelapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding
import com.mte.marvelapp.ui.adapter.listener.SeeAllClickListener

class EventsRecyclerAdapter (private val eventsAdapter: EventsAdapter,
                             private val seeAllClickListener: SeeAllClickListener
) : RecyclerView.Adapter<EventsRecyclerAdapter.EventsRecyclerViewHolder>() {

    private var shimmerVisibility = true

    class EventsRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val eventsAdapter: EventsAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seeAllClickListener: SeeAllClickListener, shimmerVisibility : Boolean){
            binding.rvCategories.adapter = eventsAdapter
            binding.categoryTitle.text = "Events"
            binding.seeAllTitle.setOnClickListener {
                seeAllClickListener.onSeeAllClick("events")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventsRecyclerViewHolder(binding,eventsAdapter)
    }

    override fun onBindViewHolder(holder: EventsRecyclerViewHolder, position: Int) {
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