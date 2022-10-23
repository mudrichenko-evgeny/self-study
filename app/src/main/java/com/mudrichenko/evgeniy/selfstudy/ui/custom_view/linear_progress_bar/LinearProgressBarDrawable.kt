package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.linear_progress_bar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.*
import android.view.Gravity
import com.mudrichenko.evgeniy.selfstudy.R

fun getLinearProgressBarDrawable(
    context: Context,
    backgroundColor: Int,
    progressColor: Int,
): Drawable {
    val backgroundShape = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = context.resources.getDimensionPixelSize(R.dimen.radius_corner).toFloat()
        color = ColorStateList.valueOf(backgroundColor)
    }

    val progressShape = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = context.resources.getDimensionPixelSize(R.dimen.radius_corner).toFloat()
        color = ColorStateList.valueOf(progressColor)
    }

    return LayerDrawable(
        arrayOf(
            backgroundShape,
            ClipDrawable(progressShape, Gravity.LEFT, ClipDrawable.HORIZONTAL)
        )
    )
}