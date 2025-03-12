package com.mudrichenkoevgeny.selfstudy.ui.custom_view.text_input

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.extensions.disableParentScroll
import com.mudrichenkoevgeny.selfstudy.extensions.setScrollbarStyle

class TextInputView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

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
        disableParentScroll()
    }

}