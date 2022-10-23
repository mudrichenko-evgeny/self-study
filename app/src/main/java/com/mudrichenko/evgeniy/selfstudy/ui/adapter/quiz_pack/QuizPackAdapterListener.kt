package com.mudrichenko.evgeniy.selfstudy.ui.adapter.quiz_pack

import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizPack

interface QuizPackAdapterListener {
    fun onEditClicked(quizPackAdapterItemVM: QuizPackAdapterItemVM)
    fun onStatisticsClicked(quizPackAdapterItemVM: QuizPackAdapterItemVM)
    fun onDeleteClicked(quizPack: QuizPack)
    fun onSetActiveClicked(quizPack: QuizPack)
    fun onHelpExpand()
}