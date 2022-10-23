package com.mudrichenko.evgeniy.selfstudy.ui.adapter.question

import android.content.Context
import android.graphics.Rect
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.ui.item_decoration.AdapterItemDecoration

class QuestionAdapterItemDecoration(private val marginBottom: Int): AdapterItemDecoration() {

    override fun setItemOffsets(context: Context, outRect: Rect, itemPosition: ItemPosition) {
        val marginHorizontal = context.resources.getDimension(R.dimen.padding_items).toInt()
        val marginVertical = context.resources.getDimension(R.dimen.padding_items).toInt()
        outRect.apply {
            left = marginHorizontal
            right = marginHorizontal
        }
        when(itemPosition) {
            ItemPosition.ONLY_ONE -> {
                outRect.apply {
                    top = marginVertical
                }
            }
            ItemPosition.FIRST -> {
                outRect.apply {
                    top = marginVertical
                    bottom = marginVertical
                }
            }
            ItemPosition.MIDDLE -> {
                outRect.apply {
                    bottom = marginVertical
                }
            }
            ItemPosition.LAST -> {
                outRect.apply {
                    bottom = marginVertical + marginBottom
                }
            }
            ItemPosition.NONE -> { }
        }
    }

}