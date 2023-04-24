package com.thrashed.flibuster.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thrashed.flibuster.data.models.ui.OnboardActionType
import com.thrashed.flibuster.data.models.ui.OnboardingItem
import com.thrashed.flibuster.databinding.ItemOnboardingBinding

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    var items = emptyList<OnboardingItem>()

    var onItemClick: ((OnboardActionType, Int) -> Unit)? = null

    class OnboardingViewHolder(val binding: ItemOnboardingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            tvOnboardingItemTitle.text = item.title
            tvOnboardingItemDescription.text = item.description
            sivOnboardingItem.setImageDrawable(item.drawable)
            clItemOnboarding.setOnClickListener { onItemClick?.invoke(item.actionType, position) }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(items: List<OnboardingItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}