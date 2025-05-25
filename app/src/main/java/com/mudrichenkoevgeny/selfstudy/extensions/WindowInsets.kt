package com.mudrichenkoevgeny.selfstudy.extensions

import android.view.*
import androidx.core.view.updatePadding

/**
 * Apply WindowInsets to View for edge-to-edge support.
 * @param top adding padding to top of view. Can be used to display content under the statusBar
 * @param bottom adding padding to bottom of view. Can be used to display content under the
 * navigationBar
 * @param left adding padding to left of view. May be needed for landscape orientation
 * @param right adding padding to right of view. May be needed for landscape orientation
 * @param applyToChild applying insets only for childView
 */
fun View.applyInsets(
    top: Boolean = false,
    bottom: Boolean = false,
    left: Boolean = true,
    right: Boolean = true,
    applyToChild: Boolean = false
) {
    if (applyToChild) {
        if (this is ViewGroup) {
            val child: View? = this.getChildAt(0)
            child?.applyInsets(
                top = top,
                bottom = bottom,
                left = left,
                right = right,
                applyToChild = false
            )
        }
        return
    }
    this.doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(
            top = padding.top + if (top) insets.getTopInset() else 0,
            bottom = padding.bottom + if (bottom) insets.getBottomInset() else 0,
            left = padding.left + if (left) insets.getLeftInset() else 0,
            right = padding.right + if (right) insets.getRightInset() else 0
        )
    }
}

@Suppress("DEPRECATION")
fun WindowInsets.getTopInset(): Int {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        this.getInsets(WindowInsets.Type.ime() or WindowInsets.Type.systemBars()).top
    } else {
        this.systemWindowInsetTop
    }
}

@Suppress("DEPRECATION")
fun WindowInsets.getBottomInset(): Int {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        this.getInsets(WindowInsets.Type.ime() or WindowInsets.Type.systemBars()).bottom
    } else {
        this.systemWindowInsetBottom
    }
}

@Suppress("DEPRECATION")
fun WindowInsets.getLeftInset(): Int {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        this.getInsets(WindowInsets.Type.ime() or WindowInsets.Type.systemBars()).left
    } else {
        this.systemWindowInsetLeft
    }
}

@Suppress("DEPRECATION")
fun WindowInsets.getRightInset(): Int {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        this.getInsets(WindowInsets.Type.ime() or WindowInsets.Type.systemBars()).right
    } else {
        this.systemWindowInsetRight
    }
}

fun WindowInsets.getHeightInset(): Int {
    return getTopInset() + getBottomInset()
}

private fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        insets
    }
    requestApplyInsetsWhenAttached()
}

data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

@Suppress("DEPRECATION")
fun WindowManager.getBoundsHeight(): Int {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        currentWindowMetrics.bounds.height()
    } else {
        defaultDisplay.height
    }
}