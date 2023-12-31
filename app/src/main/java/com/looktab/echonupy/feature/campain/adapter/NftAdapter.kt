package com.looktab.echonupy.feature.campain.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.looktab.echonupy.R
import com.looktab.echonupy.databinding.ItemNftRowBinding
import com.looktab.echonupy.feature.campain.model.Campaign

class NftAdapter : ListAdapter<Campaign, NftAdapter.HistoryCardViewHolder>
    (NftCardComparator) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryCardViewHolder {
        return HistoryCardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_nft_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class HistoryCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemNftRowBinding? =
            DataBindingUtil.bind(itemView)

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: Campaign) {
            binding?.let {
                binding.name = item.name
                binding.image = item.metadataJson.image
                binding.layoutBastItem.setOnClickListener {
                    mListener?.onItemClick(item)
                }

            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Campaign)
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }
}

object NftCardComparator : DiffUtil.ItemCallback<Campaign>() {
    override fun areItemsTheSame(
        oldItem: Campaign,
        newItem: Campaign
    ): Boolean {
        return oldItem.uri == newItem.uri
    }

    override fun areContentsTheSame(
        oldItem: Campaign,
        newItem: Campaign
    ): Boolean {
        return oldItem == newItem
    }
}