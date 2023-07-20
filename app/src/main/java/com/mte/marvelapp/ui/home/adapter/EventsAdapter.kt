package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.data.remote.model.event.Events
import com.mte.marvelapp.databinding.RecyclerEventsLayoutBinding
import com.mte.marvelapp.ui.home.adapter.listener.EventsClickListener


class EventsAdapter (private val eventsClickListener : EventsClickListener) : PagingDataAdapter<Events, EventsAdapter.EventsViewHolder>(diffUtil) {

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<Events>(){
            override fun areItemsTheSame(oldItem: Events, newItem: Events): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Events, newItem: Events): Boolean {
                return oldItem == newItem
            }

        }
    }

    class EventsViewHolder (private val binding : RecyclerEventsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (events: Events, eventsClickListener: EventsClickListener){
            binding.events = events
            binding.clickListener = eventsClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val binding = RecyclerEventsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventsAdapter.EventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem,eventsClickListener)
        }
    }

}