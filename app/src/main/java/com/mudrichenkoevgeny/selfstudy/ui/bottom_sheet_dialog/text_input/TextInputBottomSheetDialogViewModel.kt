package com.mudrichenkoevgeny.selfstudy.ui.bottom_sheet_dialog.text_input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mudrichenkoevgeny.selfstudy.ui.base.bottom_sheet_dialog.BaseBottomSheetDialogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextInputBottomSheetDialogViewModel @Inject constructor() : BaseBottomSheetDialogViewModel() {

    val textContent: MutableLiveData<String> = MutableLiveData<String>()

    private var _contentCanBeEmpty = MutableLiveData<Boolean>()
    val contentCanBeEmpty: LiveData<Boolean> = _contentCanBeEmpty

    private var _titleText = MutableLiveData<String>()
    val titleText: LiveData<String> = _titleText

    private var _hintText = MutableLiveData<String>()
    val hintText: LiveData<String> = _hintText

    private var _confirmButtonText = MutableLiveData<String>()
    val confirmButtonText: LiveData<String> = _confirmButtonText

    var extraData: String? = null

    fun setContent(
        titleText: String?,
        textContent: String?,
        contentCanBeEmpty: Boolean,
        hintText: String?,
        confirmButtonText: String?,
        extraData: String?
    ) {
        _titleText.value = titleText ?: ""
        this.textContent.value = textContent ?: ""
        _contentCanBeEmpty.value = contentCanBeEmpty
        _hintText.value = hintText ?: ""
        _confirmButtonText.value = confirmButtonText ?: ""
        this.extraData = extraData
    }

}