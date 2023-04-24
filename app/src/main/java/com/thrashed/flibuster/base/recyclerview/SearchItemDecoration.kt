package com.thrashed.flibuster.base.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.thrashed.flibuster.base.values.dp

class SearchItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == (parent.adapter?.itemCount?.minus(1) ?: return)) {
            outRect.bottom = 60.dp
        }
    }
}