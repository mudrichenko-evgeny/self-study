package com.mudrichenko.evgeniy.selfstudy.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getDrawable(@DrawableRes drawableResId: Int?): Drawable? {
    drawableResId ?: return null
    return ContextCompat.getDrawable(this, drawableResId)?.mutate()
}

fun Context.getColor(@ColorRes colorResId: Int?): Int? {
    colorResId ?: return null
    return ContextCompat.getColor(this, colorResId)
}