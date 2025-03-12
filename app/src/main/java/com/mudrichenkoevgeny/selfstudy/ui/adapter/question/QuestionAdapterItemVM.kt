package com.mudrichenkoevgeny.selfstudy.ui.adapter.question

import androidx.lifecycle.MutableLiveData
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.Question

data class QuestionAdapterItemVM(
    val questionLiveData: MutableLiveData<Question>,
    val expanded: MutableLiveData<Boolean> = MutableLiveData(false)
) {

    fun getId(): Long? = questionLiveData.value?.id

}