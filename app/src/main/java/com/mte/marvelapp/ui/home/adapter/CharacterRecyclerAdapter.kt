package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding

class CharacterRecyclerAdapter (private val characterAdapter: CharacterAdapter) : RecyclerView.Adapter<CharacterRecyclerAdapter.CharacterRecyclerViewHolder>() {

    class CharacterRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val characterAdapter: CharacterAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.rvCategories.adapter = characterAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CharacterRecyclerViewHolder(binding,characterAdapter)
    }

    override fun onBindViewHolder(holder: CharacterRecyclerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}