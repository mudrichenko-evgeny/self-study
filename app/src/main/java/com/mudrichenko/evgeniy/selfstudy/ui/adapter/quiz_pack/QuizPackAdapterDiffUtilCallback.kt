package com.mudrichenko.evgeniy.selfstudy.ui.adapter.quiz_pack

import com.mudrichenko.evgeniy.selfstudy.ui.base.adapter.BaseDiffUtilCallback

class QuizPackAdapterDiffUtilCallback : BaseDiffUtilCallback<QuizPackAdapterItemVM>() {

    override fun areItemsTheSame(
        oldItem: QuizPackAdapterItemVM,
        newItem: QuizPackAdapterItemVM
    ): Boolean {
        return oldItem.getId() == oldItem.getId()
    }

    override fun areContentsTheSame(
        oldItem: QuizPackAdapterItemVM,
        newItem: QuizPackAdapterItemVM
    ): Boolean {
        return oldItem == newItem
    }

}