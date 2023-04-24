package com.thrashed.flibuster.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thrashed.flibuster.databinding.ItemSearchSuggestionBinding

class SearchSuggestionsAdapter :
    RecyclerView.Adapter<SearchSuggestionsAdapter.SearchSuggestionViewHolder>() {

    class SearchSuggestionViewHolder(val binding: ItemSearchSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var items: List<String> = emptyList()

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestionViewHolder {
        val binding = ItemSearchSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchSuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchSuggestionViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            tvItemSearchSuggestion.text = item
            tvItemSearchSuggestion.setOnClickListener { onItemClick?.invoke(item) }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(list: List<String>?) {
        list?.let {
            items = it
            notifyDataSetChanged()
        }

    }
}