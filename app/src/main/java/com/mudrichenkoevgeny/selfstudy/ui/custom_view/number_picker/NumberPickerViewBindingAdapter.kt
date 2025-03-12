package com.mudrichenkoevgeny.selfstudy.ui.custom_view.number_picker

import androidx.databinding.BindingAdapter

@BindingAdapter("listener")
fun NumberPickerView.bindListener(listener: NumberPickerView.Listener?) {
    setListener(listener)
}

@BindingAdapter("number", "itemsList", requireAll = true)
fun <T> NumberPickerView.bindNumberPickerData(number: Int?, itemsList: List<T>) {
    setMinNumber(1)
    setMaxNumber(itemsList.size)
    setNumber(number ?: itemsList.size)
}