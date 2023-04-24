package com.thrashed.flibuster.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.thrashed.flibuster.R
import com.thrashed.flibuster.data.models.search.SearchItem
import com.thrashed.flibuster.data.models.search.SearchLinkType
import com.thrashed.flibuster.databinding.ItemSearchBinding

class SearchPagingAdapter : PagingDataAdapter<SearchItem, SearchPagingAdapter.SearchViewHolder>(SearchDiffCallback()) {

    var onItemClick: ((SearchItem) -> Unit)? = null

    class SearchViewHolder(val binding: ItemSearchBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            bindItem(holder.binding, item)
        } else {
            bindSkeleton(holder.binding)
        }
    }

    private fun bindSkeleton(binding: ItemSearchBinding) {
        binding.apply {
            clItemSearchSkeleton.isVisible = true
            clItemSearch.isVisible = false
        }
    }

    private fun bindItem(binding: ItemSearchBinding, item: SearchItem) {
        binding.apply {
            clItemSearchSkeleton.isVisible = false
            clItemSearch.isVisible = true
            root.setOnClickListener {
                onItemClick?.invoke(item)
            }
            item.link?.firstOrNull { it.rel == SearchLinkType.TITLE.value }?.let {
                sivItemSearch.load("http://flib.flibusta.is${it.href}")
            } ?: kotlin.run {
                sivItemSearch.load(R.drawable.img_default)
            }
            tvItemSearchTitle.text = item.title?.trimStart()

            item.author?.let { searchAuthorItems ->
                tvItemSearchAuthor.text = searchAuthorItems.joinToString { it.name.toString() }
            }
        }
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem == newItem
        }
    }
}