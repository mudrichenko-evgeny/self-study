package com.mudrichenko.evgeniy.selfstudy.ui.base.activity

import androidx.lifecycle.LiveData
import com.mudrichenko.evgeniy.selfstudy.data.SingleLiveEvent

interface ActivityEventInterface {
    fun<T> LiveData<T>.observeData(f: (T) -> Unit)
    fun<T> LiveData<SingleLiveEvent<T>>.observeSingleLiveEvent(f: (T) -> Unit)
}