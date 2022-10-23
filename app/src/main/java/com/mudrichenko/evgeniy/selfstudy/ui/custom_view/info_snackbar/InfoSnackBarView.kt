package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.info_snackbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.ContentViewCallback
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.utils.animationSnackBar

class InfoSnackBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {

    init {
        background = null
        View.inflate(context, R.layout.info_snack_bar, this)
        clipToPadding = false
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        animationSnackBar().start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {
        animationSnackBar().start()
    }

}
