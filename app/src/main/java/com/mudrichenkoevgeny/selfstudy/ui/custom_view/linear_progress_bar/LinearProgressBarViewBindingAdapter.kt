package com.mudrichenkoevgeny.selfstudy.ui.custom_view.linear_progress_bar

import androidx.databinding.BindingAdapter
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenkoevgeny.selfstudy.extensions.getNumberOfCorrectAnswers
import com.mudrichenkoevgeny.selfstudy.extensions.getNumberOfPassedAndCheckedQuestions
import com.mudrichenkoevgeny.selfstudy.extensions.getNumberOfPassedQuestions

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