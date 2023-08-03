package com.mte.marvelapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.ui.adapter.listener.CharacterClickListener
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.databinding.RecyclerHeroesLayoutBinding

class CharacterAdapter (private val characterClickListener: CharacterClickListener) : PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(
    diffUtil
) {

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }

    class CharacterViewHolder (private val binding : RecyclerHeroesLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(character: Character,characterClickListener: CharacterClickListener){
            binding.heroes = character
            binding.clickListener = characterClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = RecyclerHeroesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem,characterClickListener)
        }
    }

}