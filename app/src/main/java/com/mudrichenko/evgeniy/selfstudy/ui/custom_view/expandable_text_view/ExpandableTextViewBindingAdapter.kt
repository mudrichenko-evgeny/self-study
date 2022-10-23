package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.expandable_text_view

import androidx.databinding.BindingAdapter

@BindingAdapter("expanded")
fun ExpandableTextView.bindExpanded(expanded: Boolean) {
    setExpanded(expanded)
}