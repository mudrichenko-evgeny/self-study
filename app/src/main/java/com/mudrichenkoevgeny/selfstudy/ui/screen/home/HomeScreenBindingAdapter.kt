package com.mudrichenkoevgeny.selfstudy.ui.screen.home

import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizStatus
import com.mudrichenkoevgeny.selfstudy.extensions.getQuizStatus

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

