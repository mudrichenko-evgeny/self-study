package com.mudrichenko.evgeniy.selfstudy.ui.adapter.question

import com.mudrichenko.evgeniy.selfstudy.ui.base.adapter.BaseDiffUtilCallback

class QuestionAdapterDiffUtilCallback : BaseDiffUtilCallback<QuestionAdapterItemVM>() {

    override fun areItemsTheSame(
        oldItem: QuestionAdapterItemVM,
        newItem: QuestionAdapterItemVM
    ): Boolean {
        return oldItem.getId() == oldItem.getId()
    }

    override fun areContentsTheSame(
        oldItem: QuestionAdapterItemVM,
        newItem: QuestionAdapterItemVM
    ): Boolean {
        return oldItem == newItem
    }

}