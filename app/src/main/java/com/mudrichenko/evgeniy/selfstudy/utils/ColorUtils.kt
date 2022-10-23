package com.mudrichenko.evgeniy.selfstudy.utils

import android.content.res.ColorStateList

fun colorStateListChecked(
    checkedColor: Int, uncheckedColor: Int
): ColorStateList {
    return ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        ),
        intArrayOf(
            checkedColor,
            uncheckedColor
        )
    )
}