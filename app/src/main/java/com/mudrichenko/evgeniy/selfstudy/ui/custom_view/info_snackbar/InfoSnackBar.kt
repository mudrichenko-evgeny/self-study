package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.info_snackbar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.databinding.InfoSnackBarBinding
import com.mudrichenko.evgeniy.selfstudy.extensions.getBottomInset

class InfoSnackBar(
    parent: ViewGroup,
    content: InfoSnackBarView,
    val binding: InfoSnackBarBinding
) : BaseTransientBottomBar<InfoSnackBar>(parent, content, content) {

    private var snackBarLayout: Snackbar.SnackbarLayout? = null

    init {
        getView().let { view ->
            if (view is Snackbar.SnackbarLayout) {
                view.setBackgroundColor(Color.TRANSPARENT)
                view.setPadding(0, 0, 0, 0)
                snackBarLayout = view
            }
        }
    }

    fun setText(@StringRes textResId: Int) {
        binding.textView.text = context.getString(textResId)
    }

    fun setText(text: String) {
        binding.textView.text = text
    }

    companion object {

        fun make(
            containerView: View,
            infoSnackBarModel: InfoSnackBarModel,
            infoSnackBarParentType: InfoSnackBarParentType
        ): InfoSnackBar {

            val parent = containerView.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            val customView = LayoutInflater.from(containerView.context).inflate(
                R.layout.info_snack_bar_base_view,
                parent,
                false
            ) as InfoSnackBarView

            when (infoSnackBarParentType) {
                InfoSnackBarParentType.BOTTOM_SHEET_DIALOG -> {
                    containerView.parent?.let { containerParent ->
                        if (containerParent is ViewGroup) {
                            containerParent.setOnApplyWindowInsetsListener { _, windowInsets ->
                                customView.setPadding(0, 0, 0, windowInsets.getBottomInset())
                                windowInsets
                            }
                        }
                    }
                }
                else -> { }
            }

            val inflater: LayoutInflater = LayoutInflater.from(containerView.context)

            val binding: InfoSnackBarBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.info_snack_bar,
                customView,
                true
            )

            return InfoSnackBar(
                parent,
                customView,
                binding
            ).apply {
                // text
                if (infoSnackBarModel.textResId != null)
                    setText(infoSnackBarModel.textResId)
                else
                    setText(infoSnackBarModel.text ?: "")
            }
        }

        private fun View?.findSuitableParent(): ViewGroup? {
            var view = this
            var fallback: ViewGroup? = null
            do {
                if (view is CoordinatorLayout) {
                    return view
                } else if (view is FrameLayout) {
                    if (view.id == android.R.id.content) {
                        return view
                    } else {
                        fallback = view
                    }
                }
                if (view != null) {
                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)
            return fallback
        }

    }

}