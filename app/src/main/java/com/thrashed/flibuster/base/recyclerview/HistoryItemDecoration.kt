package com.thrashed.flibuster.base.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.thrashed.flibuster.base.values.dp
import com.thrashed.flibuster.ui.home.HomeHistoryAdapter

class HistoryItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val viewType = parent.adapter?.getItemViewType(position)

        if (viewType == HomeHistoryAdapter.ItemType.BOOK.value) {
            if (position == 0) {
                outRect.left = 16.dp
            } else {
                outRect.left = 2.dp
            }
            outRect.right = 2.dp
        } else {
            outRect.left = 8.dp
            outRect.right = 8.dp
        }
    }
}