@file:Suppress("unused")

package com.mudrichenkoevgeny.selfstudy.extensions

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.SeekBar
import androidx.core.view.forEach
import androidx.core.view.marginBottom
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun View.addOnDrawFinishedListener(isInfiniteOnDrawTracking: Boolean = true, callback: () -> Unit) {
    val view = this
    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredHeight > 0 && measuredWidth > 0) {
                callback()
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    }
    if (isInfiniteOnDrawTracking) {
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }
}

fun SeekBar.setProgressChangedListener(callback: (progress: Int) -> Unit) {
    setOnSeekBarChangeListener(
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.progress?.let { progress ->
                    callback.invoke(progress)
                }
            }
        }
    )
}

@SuppressLint("PrivateResource")
fun FloatingActionButton.getHeightWithMargin(): Int {
    return (marginBottom * 2) + resources
        .getDimension(com.google.android.material.R.dimen.design_fab_size_normal)
        .toInt()
}

fun View.setVisibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setVisibleOrInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun ViewGroup.getHeightForAllViewWithExclude(excludedViewIds: List<Int>): Int {
    var height = 0
    forEach { childView ->
        if (!excludedViewIds.contains(childView.id)) height += childView.height
    }
    return height
}

fun ViewGroup.getVerticalMarginsForAllViews(): Int {
    var margin = 0
    forEach { childView ->
        val layoutParams = childView.layoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            margin += layoutParams.topMargin + layoutParams.bottomMargin
        }
    }
    return margin
}

fun View.disableParentScroll() {
    setOnTouchListener { view, motionEvent ->
        view.parent.requestDisallowInterceptTouchEvent(true)
        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> {
                view.parent.requestDisallowInterceptTouchEvent(false)
                performClick()
            }
        }
        false
    }
}