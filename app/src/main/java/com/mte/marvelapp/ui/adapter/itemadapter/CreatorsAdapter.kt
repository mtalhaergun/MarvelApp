package com.mte.marvelapp.ui.adapter.itemadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.data.remote.model.creator.Creator
import com.mte.marvelapp.databinding.RecyclerCreatorsLayoutBinding
import com.mte.marvelapp.ui.adapter.listener.CreatorsClickListener

class CreatorsAdapter  (private val creatorsClickListener: CreatorsClickListener) : PagingDataAdapter<Creator, CreatorsAdapter.CreatorsViewHolder>(
    diffUtil
) {

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<Creator>(){
            override fun areItemsTheSame(oldItem: Creator, newItem: Creator): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Creator, newItem: Creator): Boolean {
                return oldItem == newItem
            }

        }
    }

    class CreatorsViewHolder (private val binding : RecyclerCreatorsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (creators: Creator, creatorsClickListener: CreatorsClickListener){
            binding.creators = creators
            binding.clickListener = creatorsClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorsViewHolder {
        val binding = RecyclerCreatorsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CreatorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreatorsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem,creatorsClickListener)
        }
    }

}