package com.thrashed.flibuster.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thrashed.flibuster.databinding.ItemLoadingBinding

class SearchStateAdapter: LoadStateAdapter<SearchStateAdapter.LoadingViewHolder>() {

    class LoadingViewHolder(val binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingViewHolder(binding)
    }
}