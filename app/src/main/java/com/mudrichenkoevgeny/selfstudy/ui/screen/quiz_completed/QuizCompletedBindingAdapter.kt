package com.mudrichenkoevgeny.selfstudy.ui.screen.quiz_completed

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenkoevgeny.selfstudy.extensions.getNumberOfCorrectAnswers
import com.mudrichenkoevgeny.selfstudy.extensions.getNumberOfPassedAndCheckedQuestions

@BindingAdapter("statisticsQuizQuestionList")
fun TextView.bindQuizStatistics(quizQuestionList: List<QuizQuestion>) {
    text = resources.getString(
        R.string.quiz_statistics,
        quizQuestionList.getNumberOfPassedAndCheckedQuestions(),
        quizQuestionList.getNumberOfCorrectAnswers()
    )
}