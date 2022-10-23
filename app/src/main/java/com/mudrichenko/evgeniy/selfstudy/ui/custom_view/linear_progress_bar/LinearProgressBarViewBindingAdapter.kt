package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.linear_progress_bar

import androidx.databinding.BindingAdapter
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenko.evgeniy.selfstudy.extensions.getNumberOfCorrectAnswers
import com.mudrichenko.evgeniy.selfstudy.extensions.getNumberOfPassedAndCheckedQuestions
import com.mudrichenko.evgeniy.selfstudy.extensions.getNumberOfPassedQuestions

@BindingAdapter("progress")
fun LinearProgressBarView.bindProgress(progress: Int, max: Int) {
    setProgress(progress, max)
}

@BindingAdapter("progressQuizQuestionList")
fun LinearProgressBarView.bindProgressQuizQuestionList(quizQuestionList: List<QuizQuestion>) {
    if (quizQuestionList.isEmpty()) {
        setProgress(0, 0)
        return
    }
    setProgress(quizQuestionList.getNumberOfPassedQuestions(), quizQuestionList.size)
}

@BindingAdapter("statisticsQuizQuestionList")
fun LinearProgressBarView.bindStatisticsQuizQuestionList(quizQuestionList: List<QuizQuestion>) {
    if (quizQuestionList.isEmpty()) {
        setProgress(0, 0)
        return
    }
    setProgress(
        quizQuestionList.getNumberOfCorrectAnswers(),
        quizQuestionList.getNumberOfPassedAndCheckedQuestions()
    )
}