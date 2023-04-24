package com.thrashed.flibuster.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.thrashed.flibuster.R
import com.thrashed.flibuster.data.models.search.SearchItem
import com.thrashed.flibuster.data.models.search.SearchLinkType
import com.thrashed.flibuster.databinding.ItemSearchBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var items: List<SearchItem?> = emptyList()

    var onItemClick: ((SearchItem) -> Unit)? = null

    class SearchViewHolder(val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            root.setOnClickListener {
                if (item != null) onItemClick?.invoke(item)
            }
            item?.link?.firstOrNull { it.rel == SearchLinkType.TITLE.value }?.let {
                sivItemSearch.load("http://flib.flibusta.is${it.href}")
            } ?: kotlin.run {
                sivItemSearch.load(R.drawable.img_default)
            }
            tvItemSearchTitle.text = item?.title?.trimStart()

            item?.author?.let { searchAuthorItems ->
                tvItemSearchAuthor.text = searchAuthorItems.joinToString { it.name.toString() }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(list: List<SearchItem?>) {
        this.items = list
        notifyDataSetChanged()
    }
}