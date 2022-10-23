package com.mudrichenko.evgeniy.selfstudy.ui.adapter.question

interface QuestionAdapterListener {
    fun onEditClicked(questionAdapterItemVM: QuestionAdapterItemVM)
    fun onExpand(questionAdapterItemVM: QuestionAdapterItemVM)
}