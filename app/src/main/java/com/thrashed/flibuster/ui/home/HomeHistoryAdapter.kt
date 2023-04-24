package com.thrashed.flibuster.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.thrashed.flibuster.R
import com.thrashed.flibuster.base.imageView.ColorFilterTransformation
import com.thrashed.flibuster.data.database.entities.toBookItem
import com.thrashed.flibuster.data.models.search.SearchItem
import com.thrashed.flibuster.data.models.search.SearchLinkType
import com.thrashed.flibuster.data.models.ui.HomeHistoryItem
import com.thrashed.flibuster.databinding.ItemHomeHistoryBinding
import com.thrashed.flibuster.databinding.ItemHomeHistoryEmptyBinding

class HomeHistoryAdapter: RecyclerView.Adapter<ViewHolder>() {

    var items = emptyList<HomeHistoryItem>()

    var onItemClick: ((SearchItem) -> Unit)? = null

    class EmptyHistoryItemViewHolder(val binding: ItemHomeHistoryEmptyBinding): ViewHolder(binding.root)

    class HomeHistoryItemViewHolder(val binding: ItemHomeHistoryBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ItemType.EMPTY.value) {
            val binding = ItemHomeHistoryEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyHistoryItemViewHolder(binding)
        } else {
            val binding = ItemHomeHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HomeHistoryItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (holder is HomeHistoryItemViewHolder && item is HomeHistoryItem.Book) {
            holder.binding.tvItemHomeHistoryTitle.text = item.bookItem.title
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(item.bookItem.toBookItem())
            }
            item.bookItem.link?.firstOrNull { it.rel == SearchLinkType.TITLE.value }?.let {
                holder.binding.sivItemHomeHistory.load("http://flib.flibusta.is${it.href}") {
                    transformations(ColorFilterTransformation(holder.itemView.context.getColor(R.color.colorBlack50)))
                }
            } ?: kotlin.run {
                holder.binding.sivItemHomeHistory.load(R.drawable.img_default) {
                    transformations(ColorFilterTransformation(holder.itemView.context.getColor(R.color.colorBlack50)))
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomeHistoryItem.Book -> ItemType.BOOK.value
            is HomeHistoryItem.Empty -> ItemType.EMPTY.value
        }
    }

    enum class ItemType(val value: Int) {
        BOOK(0),
        EMPTY(1)
    }

    fun updateItems(list: List<HomeHistoryItem>) {
        this.items = list
        notifyDataSetChanged()
    }

}