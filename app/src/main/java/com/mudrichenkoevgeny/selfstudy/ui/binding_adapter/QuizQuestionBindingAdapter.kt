package com.mudrichenkoevgeny.selfstudy.ui.binding_adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizQuestion

@BindingAdapter("contentTextQuizQuestion")
fun TextView.bindContentText(quizQuestion: QuizQuestion?) {
    if (quizQuestion == null) {
        text = ""
        return
    }
    val span = SpannableString(quizQuestion.content)
    span.setSpan(
        StyleSpan(Typeface.BOLD_ITALIC),
        0,
        quizQuestion.content.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = span
}

@BindingAdapter("userAnswerTextQuizQuestion")
fun TextView.bindUserAnswerText(quizQuestion: QuizQuestion?) {
    if (quizQuestion == null) {
        text = ""
        return
    }
    val userAnswerTitle = resources.getString(R.string.your_answer)
    val spanText = userAnswerTitle + quizQuestion.getDisplayedUserAnswerText()
    val userAnswerTitleSpanEnd = userAnswerTitle.length
    val userAnswerTextSpanEnd = userAnswerTitleSpanEnd + quizQuestion.getDisplayedUserAnswerText().length

    val span = SpannableString(spanText)
    span.setSpan(
        StyleSpan(Typeface.BOLD),
        0,
        userAnswerTitle.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    span.setSpan(
        StyleSpan(Typeface.NORMAL),
        userAnswerTitle.length,
        userAnswerTextSpanEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = span
}

@BindingAdapter("answerTextQuizQuestion")
fun TextView.bindAnswerText(quizQuestion: QuizQuestion?) {
    if (quizQuestion == null) {
        text = ""
        return
    }
    val correctAnswerTitle = resources.getString(R.string.correct_answer)

    val spanText = quizQuestion.content +
            correctAnswerTitle +
            quizQuestion.correctAnswer

    val quizContentSpanEnd = quizQuestion.content.length
    val correctAnswerTitleSpanEnd = quizContentSpanEnd + correctAnswerTitle.length
    val correctAnswerTextSpanEnd = correctAnswerTitleSpanEnd + quizQuestion.correctAnswer.length

    val span = SpannableString(spanText)
    span.setSpan(
        StyleSpan(Typeface.BOLD_ITALIC),
        0,
        quizContentSpanEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    span.setSpan(
        StyleSpan(Typeface.BOLD),
        quizContentSpanEnd,
        correctAnswerTitleSpanEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    span.setSpan(
        StyleSpan(Typeface.NORMAL),
        correctAnswerTitleSpanEnd,
        correctAnswerTextSpanEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = span
}
