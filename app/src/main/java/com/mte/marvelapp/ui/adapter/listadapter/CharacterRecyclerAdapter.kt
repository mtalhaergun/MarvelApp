package com.mte.marvelapp.ui.adapter.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding
import com.mte.marvelapp.ui.adapter.itemadapter.CharacterAdapter
import com.mte.marvelapp.ui.adapter.listener.SeeAllClickListener

class CharacterRecyclerAdapter (private val characterAdapter: CharacterAdapter,
                                private val seeAllClickListener: SeeAllClickListener
) : RecyclerView.Adapter<CharacterRecyclerAdapter.CharacterRecyclerViewHolder>() {

    private var shimmerVisibility = true

    class CharacterRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val characterAdapter: CharacterAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seeAllClickListener: SeeAllClickListener, shimmerVisibility : Boolean){
            binding.rvCategories.adapter = characterAdapter
            binding.categoryTitle.text = "Heroes"
            binding.seeAllTitle.setOnClickListener {
                seeAllClickListener.onSeeAllClick("characters")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CharacterRecyclerViewHolder(binding,characterAdapter)
    }

    override fun onBindViewHolder(holder: CharacterRecyclerViewHolder, position: Int) {
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