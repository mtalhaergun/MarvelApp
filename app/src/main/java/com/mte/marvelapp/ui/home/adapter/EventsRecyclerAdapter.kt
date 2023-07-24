package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding

class EventsRecyclerAdapter (private val eventsAdapter: EventsAdapter) : RecyclerView.Adapter<EventsRecyclerAdapter.EventsRecyclerViewHolder>() {

    class EventsRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val eventsAdapter: EventsAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.rvCategories.adapter = eventsAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventsRecyclerViewHolder(binding,eventsAdapter)
    }

    override fun onBindViewHolder(holder: EventsRecyclerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}