package com.mudrichenko.evgeniy.selfstudy.extensions

import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizStatus

fun List<QuizQuestion>.getNextQuestion(): QuizQuestion? {
    return firstOrNull { quizQuestion ->
        !quizQuestion.isAnswered() || !quizQuestion.isAnswerChecked()
    }
}

fun List<QuizQuestion>.getNumberOfPassedQuestions(): Int {
    return filter { quizQuestion -> quizQuestion.isAnswered() }.size
}

fun List<QuizQuestion>.getNumberOfPassedAndCheckedQuestions(): Int {
    return filter { quizQuestion -> quizQuestion.isAnswerChecked() }.size
}

fun List<QuizQuestion>.getNumberOfCorrectAnswers(): Int {
    return filter { quizQuestion -> quizQuestion.isAnswerCorrect() }.size
}


fun List<QuizQuestion>.getQuizStatus(): QuizStatus {
    if (isEmpty()) return QuizStatus.NOT_STARTED
    getNumberOfPassedAndCheckedQuestions().let { numberOfPassedQuestions ->
        return when (numberOfPassedQuestions) {
            in 0 until size -> QuizStatus.STARTED
            else -> QuizStatus.NOT_STARTED
        }
    }
}

fun List<QuizQuestion>.hasNextQuestion(): Boolean {
    return getNumberOfPassedAndCheckedQuestions() != size
}