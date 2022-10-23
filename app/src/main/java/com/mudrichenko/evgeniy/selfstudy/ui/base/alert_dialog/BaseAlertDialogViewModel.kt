package com.mudrichenko.evgeniy.selfstudy.ui.base.alert_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mudrichenko.evgeniy.selfstudy.data.SingleLiveEvent
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragmentViewModel

abstract class BaseAlertDialogViewModel : BaseFragmentViewModel() {

    private var _dismissEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val dismissEvent: LiveData<SingleLiveEvent<Unit>> = _dismissEvent

    protected fun dismiss() {
        _dismissEvent.value = SingleLiveEvent(Unit)
    }

}