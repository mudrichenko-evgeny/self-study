package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.app_scroll_view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.extensions.disableParentScroll
import com.mudrichenko.evgeniy.selfstudy.extensions.setScrollbarStyle

class AppScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    init {
        isScrollbarFadingEnabled = false
        scrollBarStyle = SCROLLBARS_INSIDE_INSET
        this.setScrollbarStyle(
            vertical = isVerticalScrollBarEnabled,
            horizontal = isHorizontalScrollBarEnabled,
            thumbDrawableResId = R.drawable.background_rectangle,
            trackDrawableResId = R.drawable.background_rectangle,
            thumbColorResId = R.color.on_background_secondary,
            trackColorResId = R.color.background_secondary
        )

        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (canScrollVertically(1)) {
                disableParentScroll()
            }
        }
    }

}