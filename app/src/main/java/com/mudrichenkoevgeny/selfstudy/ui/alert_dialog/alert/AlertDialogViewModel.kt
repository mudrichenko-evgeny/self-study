package com.mudrichenkoevgeny.selfstudy.ui.alert_dialog.alert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mudrichenkoevgeny.selfstudy.ui.base.alert_dialog.BaseAlertDialogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlertDialogViewModel @Inject constructor() : BaseAlertDialogViewModel() {

    private var _titleText = MutableLiveData<String>()
    val titleText: LiveData<String> = _titleText

    private var _descriptionText = MutableLiveData<String>()
    val descriptionText: LiveData<String> = _descriptionText

    private var _cancelButtonText = MutableLiveData<String>()
    val cancelButtonText: LiveData<String> = _cancelButtonText

    private var _confirmButtonText = MutableLiveData<String>()
    val confirmButtonText: LiveData<String> = _confirmButtonText

    var extraData: String? = null

    fun setContent(
        titleText: String?,
        descriptionText: String?,
        cancelButtonText: String?,
        confirmButtonText: String?,
        extraData: String?
    ) {
        _titleText.value = titleText ?: ""
        _descriptionText.value = descriptionText ?: ""
        _cancelButtonText.value = cancelButtonText ?: ""
        _confirmButtonText.value = confirmButtonText ?: ""
        this.extraData = extraData
    }

}