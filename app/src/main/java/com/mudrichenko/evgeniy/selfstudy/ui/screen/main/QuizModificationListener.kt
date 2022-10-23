package com.mudrichenko.evgeniy.selfstudy.ui.screen.main

import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse

interface QuizModificationListener {
    fun onComplete(response: DataResponse<Unit>)
}