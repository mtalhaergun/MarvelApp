package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding

class ComicsRecyclerAdapter (private val comicsAdapter: ComicsAdapter) : RecyclerView.Adapter<ComicsRecyclerAdapter.ComicsRecyclerViewHolder>() {

    class ComicsRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val comicsAdapter: ComicsAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.rvCategories.adapter = comicsAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ComicsRecyclerViewHolder(binding,comicsAdapter)
    }

    override fun onBindViewHolder(holder: ComicsRecyclerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}