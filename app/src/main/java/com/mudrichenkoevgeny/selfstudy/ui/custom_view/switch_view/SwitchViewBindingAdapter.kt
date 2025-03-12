package com.mudrichenkoevgeny.selfstudy.ui.custom_view.switch_view

import androidx.databinding.BindingAdapter
import com.mudrichenkoevgeny.selfstudy.R

@BindingAdapter("switchPreferDifficultQuestions")
fun SwitchView.bindSwitchPreferDifficultQuestions(preferDifficultQuestions: Boolean?) {
    setText(
        resources.getString(
            if (preferDifficultQuestions == true)
                R.string.prefer_difficult_questions
            else
                R.string.random_questions
        )
    )
}

@BindingAdapter("switchListener")
fun SwitchView.bindSwitchListener(listener: SwitchView.Listener?) {
    this.listener = listener
}