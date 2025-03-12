package com.mudrichenkoevgeny.selfstudy.ui.binding_adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.Question

@BindingAdapter("infoTextQuestion")
fun TextView.bindInfoText(question: Question?) {
    if (question == null) {
        text = ""
        return
    }
    val span = SpannableString("${question.content}\n${question.answer}")
    span.setSpan(
        StyleSpan(Typeface.BOLD_ITALIC),
        0,
        question.content.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    span.setSpan(
        StyleSpan(Typeface.NORMAL),
        question.content.length,
        span.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = span
}