package com.mte.marvelapp.ui.home.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding
import com.mte.marvelapp.ui.home.HomeFragment
import com.mte.marvelapp.ui.home.HomeFragmentDirections
import com.mte.marvelapp.ui.home.adapter.listener.SeeAllClickListener
import com.mte.marvelapp.utils.extensions.safeNavigate

class CharacterRecyclerAdapter (private val characterAdapter: CharacterAdapter,
                                private val seeAllClickListener: SeeAllClickListener) : RecyclerView.Adapter<CharacterRecyclerAdapter.CharacterRecyclerViewHolder>() {

    private var shimmerVisibility = true

    class CharacterRecyclerViewHolder(private val binding: RecyclerListLayoutBinding,private val characterAdapter: CharacterAdapter) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seeAllClickListener: SeeAllClickListener,shimmerVisibility : Boolean){
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
        shimmerVisibility = false
        notifyDataSetChanged()
    }

    fun startShimmer(){
        shimmerVisibility = true
        notifyDataSetChanged()
    }
}