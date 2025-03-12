package com.mudrichenkoevgeny.selfstudy.ui.adapter.question

interface QuestionAdapterListener {
    fun onEditClicked(questionAdapterItemVM: QuestionAdapterItemVM)
    fun onExpand(questionAdapterItemVM: QuestionAdapterItemVM)
}