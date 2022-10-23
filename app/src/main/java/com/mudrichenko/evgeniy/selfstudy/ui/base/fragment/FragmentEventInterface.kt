package com.mudrichenko.evgeniy.selfstudy.ui.base.fragment

import androidx.lifecycle.LiveData
import com.mudrichenko.evgeniy.selfstudy.data.SingleLiveEvent
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel

interface FragmentEventInterface {
    fun<T> LiveData<T>.observeData(f: (T) -> Unit)
    fun<T> LiveData<SingleLiveEvent<T>>.observeSingleLiveEvent(f: (T) -> Unit)
    fun showInfoSnackBar(infoSnackBarModel: InfoSnackBarModel)
}