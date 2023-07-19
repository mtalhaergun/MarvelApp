package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.data.remote.model.creator.Creator
import com.mte.marvelapp.databinding.RecyclerCreatorsLayoutBinding
import com.mte.marvelapp.databinding.RecyclerStoriesLayoutBinding
import com.mte.marvelapp.ui.home.adapter.listener.CreatorsClickListener

class CreatorsAdapter  (private val creatorsClickListener: CreatorsClickListener) : RecyclerView.Adapter<CreatorsAdapter.CreatorsViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Creator>(){
        override fun areItemsTheSame(oldItem: Creator, newItem: Creator): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Creator, newItem: Creator): Boolean {
            return oldItem == newItem
        }

    }

    private val diffList = AsyncListDiffer(this,diffUtil)
    var creators : List<Creator>
        get() = diffList.currentList
        set(value) = diffList.submitList(value)


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
        holder.bind(creators[position],creatorsClickListener)
    }

    override fun getItemCount(): Int {
        return creators.size
    }

}