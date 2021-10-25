package com.task.ui.character.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.task.data.remote.model.CharacterData
import com.task.databinding.ItemCharacterBinding
import javax.inject.Inject


class CharactersAdapter @Inject constructor() : PagingDataAdapter<CharacterData, CharactersAdapter.CharacterViewHolder>(
    DiffUtilCallBack()
) {

    var onItemClick: ((item: CharacterData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CharacterViewHolder =
        CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) =
        holder.bind(getItem(position)!!)


    class CharacterViewHolder(
        private var binding: ItemCharacterBinding,
        private val onItemClick: ((character: CharacterData) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character : CharacterData) {

            binding.name.text = character.name
            Glide.with(binding.root)
                .load(character.image)
                .transform(CircleCrop())
                .into(binding.image)

            itemView.setOnClickListener {
                character.let { onItemClick?.invoke(it) }
            }
        }
    }


    class DiffUtilCallBack : DiffUtil.ItemCallback<CharacterData>(){
        override fun areItemsTheSame(oldItem: CharacterData, newItem: CharacterData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CharacterData, newItem: CharacterData): Boolean {
            return oldItem == newItem
        }

    }



}