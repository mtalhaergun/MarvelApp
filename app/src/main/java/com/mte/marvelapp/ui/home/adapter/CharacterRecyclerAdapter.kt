package com.mte.marvelapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.databinding.RecyclerHeroesLayoutBinding
import com.mte.marvelapp.databinding.RecyclerListLayoutBinding
import com.mte.marvelapp.ui.home.adapter.listener.CharacterClickListener

class CharacterRecyclerAdapter : RecyclerView.Adapter<CharacterRecyclerAdapter.CharacterRecyclerViewHolder>() {

    class CharacterRecyclerViewHolder(private val binding: RecyclerListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.rvCategories.adapter = CharacterAdapter(object : CharacterClickListener{
                override fun onCharacterClick(character: Character) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterRecyclerViewHolder {
        val binding = RecyclerListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CharacterRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterRecyclerViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 1
}