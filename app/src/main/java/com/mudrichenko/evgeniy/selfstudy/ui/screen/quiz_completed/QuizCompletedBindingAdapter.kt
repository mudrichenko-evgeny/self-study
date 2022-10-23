package com.mudrichenko.evgeniy.selfstudy.ui.screen.quiz_completed

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenko.evgeniy.selfstudy.extensions.getNumberOfCorrectAnswers
import com.mudrichenko.evgeniy.selfstudy.extensions.getNumberOfPassedAndCheckedQuestions

@BindingAdapter("statisticsQuizQuestionList")
fun TextView.bindQuizStatistics(quizQuestionList: List<QuizQuestion>) {
    text = resources.getString(
        R.string.quiz_statistics,
        quizQuestionList.getNumberOfPassedAndCheckedQuestions(),
        quizQuestionList.getNumberOfCorrectAnswers()
    )
}