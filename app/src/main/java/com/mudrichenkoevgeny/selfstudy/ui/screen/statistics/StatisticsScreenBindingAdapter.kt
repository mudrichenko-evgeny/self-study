package com.mudrichenkoevgeny.selfstudy.ui.screen.statistics

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.SortType
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.circle_button.CircleButton

@BindingAdapter("sortType")
fun CircleButton.bindSortTypeImage(sortType: SortType?) {
    setImageDrawable(
        ContextCompat.getDrawable(
            context,
            if (sortType == SortType.DESCENDING)
                R.drawable.ic_sort_descending
            else
                R.drawable.ic_sort_ascending
        )
    )
}