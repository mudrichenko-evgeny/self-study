package com.mudrichenkoevgeny.selfstudy.ui.base.activity

import androidx.lifecycle.LiveData
import com.mudrichenkoevgeny.selfstudy.data.SingleLiveEvent

interface ActivityEventInterface {
    fun<T> LiveData<T>.observeData(f: (T) -> Unit)
    fun<T> LiveData<SingleLiveEvent<T>>.observeSingleLiveEvent(f: (T) -> Unit)
}