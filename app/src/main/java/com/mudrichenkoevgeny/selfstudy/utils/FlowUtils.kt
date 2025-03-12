package com.mudrichenkoevgeny.selfstudy.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

fun tickerFlow(period: Duration) = flow {
    var count = 0
    while (true) {
        emit(count)
        delay(period)
        count++
    }
}