package com.mudrichenko.evgeniy.selfstudy.ui.base.bottom_sheet_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mudrichenko.evgeniy.selfstudy.data.SingleLiveEvent
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragmentViewModel

abstract class BaseBottomSheetDialogViewModel : BaseFragmentViewModel() {

    var contentMaxHeight: Int = 0

    private var _dismissEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val dismissEvent: LiveData<SingleLiveEvent<Unit>> = _dismissEvent

    protected fun dismiss() {
        _dismissEvent.value = SingleLiveEvent(Unit)
    }

}