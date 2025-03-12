@file:Suppress("unused")

package com.mudrichenkoevgeny.selfstudy.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.millisToSeconds(): Int {
    return (this / 1_000).toInt()
}

fun Int.secondsToMillis(): Long {
    return this * 1_000L
}

fun Long.millisToFormattedTime(): String {
    val date = Date()
    date.time = this
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)
}

fun Int.secondsToFormattedTime(): String {
    val date = Date()
    date.time = this * 1_000L
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)
}