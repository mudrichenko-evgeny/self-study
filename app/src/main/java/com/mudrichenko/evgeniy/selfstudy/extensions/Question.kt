package com.mudrichenko.evgeniy.selfstudy.extensions

import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.Question
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.SortType

fun List<Question>.getSortedList(sortType: SortType?): List<Question> {
    val questionsWithoutAttemptsList: List<Question> = filter { question ->
        question.attemptCount <= 0
    }
    val questionsWithAttemptsList: List<Question> = filter { question ->
        question.attemptCount > 0
    }
    val sortedList: MutableList<Question> = mutableListOf()
    sortedList.addAll(
        when (sortType) {
            SortType.ASCENDING -> questionsWithAttemptsList.sortedBy { it.getCorrectPercent() }
            SortType.DESCENDING -> questionsWithAttemptsList.sortedByDescending { it.getCorrectPercent() }
            else -> this
        }
    )
    sortedList.addAll(questionsWithoutAttemptsList)
    return sortedList
}