package com.mudrichenkoevgeny.selfstudy.ui.custom_view.expandable_text_view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class ExpandableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    fun setExpanded(expanded: Boolean) {
        if (expanded) {
            this.maxLines = Int.MAX_VALUE
        } else {
            this.maxLines = 1
        }
    }

}