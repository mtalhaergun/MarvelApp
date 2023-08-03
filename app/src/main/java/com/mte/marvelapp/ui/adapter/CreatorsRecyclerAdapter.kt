package com.mte.marvelapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding
import com.mte.marvelapp.ui.adapter.listener.SeeAllClickListener

class CreatorsRecyclerAdapter (private val creatorsAdapter: CreatorsAdapter,
                               private val seeAllClickListener: SeeAllClickListener
) : RecyclerView.Adapter<CreatorsRecyclerAdapter.CreatorsRecyclerViewHolder>() {

    class CreatorsRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val creatorsAdapter: CreatorsAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seeAllClickListener: SeeAllClickListener){
            binding.rvCategories.adapter = creatorsAdapter
            binding.categoryTitle.text = "Creators"
            binding.seeAllTitle.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorsRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CreatorsRecyclerViewHolder(binding,creatorsAdapter)
    }

    override fun onBindViewHolder(holder: CreatorsRecyclerViewHolder, position: Int) {
        holder.bind(seeAllClickListener)
    }

    override fun getItemCount(): Int = 1

}