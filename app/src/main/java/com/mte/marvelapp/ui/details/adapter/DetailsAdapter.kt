package com.mte.marvelapp.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mte.marvelapp.data.remote.model.character.Character
import com.mte.marvelapp.data.remote.model.detail.DetailModel
import com.mte.marvelapp.databinding.RecyclerDetailLayoutBinding
import com.mte.marvelapp.databinding.RecyclerHeroesLayoutBinding
import com.mte.marvelapp.ui.details.adapter.listener.DetailsRecyclerClickListener
import com.mte.marvelapp.ui.home.adapter.CharacterAdapter
import com.mte.marvelapp.ui.home.adapter.listener.CharacterClickListener

class DetailsAdapter (private val detailsRecyclerClickListener: DetailsRecyclerClickListener) : RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<DetailModel>() {
        override fun areItemsTheSame(oldItem: DetailModel, newItem: DetailModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DetailModel, newItem: DetailModel): Boolean {
            return oldItem == newItem
        }

    }

    private val diffList = AsyncListDiffer(this, diffUtil)
    var listDetail: List<DetailModel>
        get() = diffList.currentList
        set(value) = diffList.submitList(value)

    class DetailsViewHolder (private val binding : RecyclerDetailLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(detailModel: DetailModel, detailsRecyclerClickListener: DetailsRecyclerClickListener){
            binding.detail = detailModel
            binding.clickListener = detailsRecyclerClickListener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val binding = RecyclerDetailLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(listDetail[position],detailsRecyclerClickListener)
    }

    override fun getItemCount(): Int {
        return listDetail.size
    }
}