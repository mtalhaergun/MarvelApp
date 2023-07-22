package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerHeaderLayoutBinding

class HeaderAdapter(private val headerText : String) : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {


    class HeaderViewHolder(private val binding: RecyclerHeaderLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(headerText: String){
            binding.categoryTitle.text = headerText
            binding.categoryTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val binding = RecyclerHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(headerText)
    }

    override fun getItemCount(): Int = 1
}