package com.mudrichenko.evgeniy.selfstudy.ui.item_decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AdapterItemDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        setItemOffsets(parent.context, outRect, getItemPosition(view, parent))
    }

    private fun getItemPosition(view: View, parent: RecyclerView): ItemPosition {
        val lastItemPosition = (parent.adapter?.itemCount ?: 0) - 1
        val itemPosition = parent.getChildAdapterPosition(view)
        return when {
            lastItemPosition == -1 -> ItemPosition.NONE
            itemPosition == 0 && itemPosition == lastItemPosition -> ItemPosition.ONLY_ONE
            itemPosition == 0 -> ItemPosition.FIRST
            itemPosition == lastItemPosition -> ItemPosition.LAST
            else -> ItemPosition.MIDDLE
        }
    }

    abstract fun setItemOffsets(context: Context, outRect: Rect, itemPosition: ItemPosition)

    enum class ItemPosition {
        FIRST, LAST, ONLY_ONE, MIDDLE, NONE
    }

}