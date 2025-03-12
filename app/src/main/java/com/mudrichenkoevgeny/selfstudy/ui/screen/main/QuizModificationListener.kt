package com.mudrichenkoevgeny.selfstudy.ui.screen.main

import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse

interface QuizModificationListener {
    fun onComplete(response: DataResponse<Unit>)
}