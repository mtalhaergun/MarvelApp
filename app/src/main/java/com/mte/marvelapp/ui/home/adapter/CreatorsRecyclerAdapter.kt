package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding

class CreatorsRecyclerAdapter (private val creatorsAdapter: CreatorsAdapter) : RecyclerView.Adapter<CreatorsRecyclerAdapter.CreatorsRecyclerViewHolder>() {

    class CreatorsRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val creatorsAdapter: CreatorsAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.rvCategories.adapter = creatorsAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorsRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CreatorsRecyclerViewHolder(binding,creatorsAdapter)
    }

    override fun onBindViewHolder(holder: CreatorsRecyclerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}