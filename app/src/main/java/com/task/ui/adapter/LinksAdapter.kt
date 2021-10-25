package com.task.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.databinding.ItemUrlBinding

class LinksAdapter : RecyclerView.Adapter<LinksAdapter.LinkViewHolder>() {

    var onItemClick: ((item: String) -> Unit)? = null

    private var items = arrayListOf<String>()

    fun setItems(items: ArrayList<String>) {
        this.items = items
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder =

        LinkViewHolder(
            ItemUrlBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )


    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int  = items.size


    class LinkViewHolder(
        private var binding: ItemUrlBinding,
        private val onItemClick: ((link: String) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(link : String) {

            binding.textViewEpisode.text = link


            itemView.setOnClickListener {
                link.let { onItemClick?.invoke(it) }
            }
        }
    }
}