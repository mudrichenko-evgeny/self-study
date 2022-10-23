package com.mudrichenko.evgeniy.selfstudy.extensions

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import java.lang.reflect.Field

@Suppress("ControlFlowWithEmptyBody")
@SuppressLint("DiscouragedPrivateApi")
fun View.setScrollbarStyle(
    vertical: Boolean = false,
    horizontal: Boolean = false,
    @DrawableRes thumbDrawableResId: Int? = null,
    @DrawableRes trackDrawableResId: Int? = null,
    @ColorRes thumbColorResId: Int? = null,
    @ColorRes trackColorResId: Int? = null
) {
    val thumbDrawable = context.getDrawable(thumbDrawableResId)?.let { drawable ->
        context.getColor(thumbColorResId)?.let { color ->
            drawable.setTint(color)
        }
        drawable
    }
    val trackDrawable = context.getDrawable(trackDrawableResId)?.let { drawable ->
        context.getColor(trackColorResId)?.let { color ->
            drawable.setTint(color)
        }
        drawable
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (thumbDrawable != null && vertical) verticalScrollbarThumbDrawable = thumbDrawable
        if (thumbDrawable != null && horizontal) horizontalScrollbarThumbDrawable = thumbDrawable
        if (trackDrawable != null && vertical) verticalScrollbarTrackDrawable = trackDrawable
        if (trackDrawable != null && horizontal) horizontalScrollbarTrackDrawable = trackDrawable
    } else {
        try {
            val mScrollCacheField: Field = View::class.java.getDeclaredField("mScrollCache")
            mScrollCacheField.isAccessible = true
            val mScrollCache = mScrollCacheField.get(this)
            val scrollBarField: Field = mScrollCache.javaClass.getDeclaredField("scrollBar")
            scrollBarField.isAccessible = true
            scrollBarField.get(mScrollCache)?.let { scrollBar ->
                if (thumbDrawable != null && vertical) {
                    scrollBar.invokeDeclaredMethodWithDrawable(
                        "setVerticalThumbDrawable",
                        Drawable::class.java,
                        thumbDrawable
                    )
                }
                if (thumbDrawable != null && horizontal) {
                    scrollBar.invokeDeclaredMethodWithDrawable(
                        "setHorizontalThumbDrawable",
                        Drawable::class.java,
                        thumbDrawable
                    )
                }
                if (trackDrawable != null && vertical) {
                    // no method
                }
                if (trackDrawable != null && horizontal) {
                    // no method
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}