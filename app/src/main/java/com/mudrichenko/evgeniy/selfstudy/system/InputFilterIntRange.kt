package com.mudrichenko.evgeniy.selfstudy.system

import android.text.InputFilter
import android.text.Spanned

class InputFilterIntRange(private val min: Int, private val max: Int) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toIntOrNull()
            if (isInRange(min, max, input))
                return null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isInRange(min: Int, max: Int, input: Int?): Boolean {
        return input in min..max
    }

}