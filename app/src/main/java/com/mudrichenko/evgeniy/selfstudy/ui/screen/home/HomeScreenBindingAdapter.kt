package com.mudrichenko.evgeniy.selfstudy.ui.screen.home

import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizStatus
import com.mudrichenko.evgeniy.selfstudy.extensions.getQuizStatus

@BindingAdapter("visibleIfStartedQuizQuestionList")
fun View.bindVisibleIfStartedQuizQuestionList(quizQuestionList: List<QuizQuestion>) {
    visibility = if (quizQuestionList.getQuizStatus() == QuizStatus.STARTED)
        View.VISIBLE
    else
        View.GONE
}

@BindingAdapter("visibleIfNotStartedQuizQuestionList")
fun View.bindVisibleIfNotStartedQuizQuestionList(quizQuestionList: List<QuizQuestion>) {
    visibility = if (quizQuestionList.getQuizStatus() == QuizStatus.NOT_STARTED)
        View.VISIBLE
    else
        View.GONE
}

