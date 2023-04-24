package com.thrashed.flibuster.base.recyclerview

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.removeAllItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}
