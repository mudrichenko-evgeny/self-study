package com.mudrichenko.evgeniy.selfstudy.data.model.`object`

import androidx.annotation.StringRes
import com.mudrichenko.evgeniy.selfstudy.R

enum class AppError {
    UNKNOWN,
    NO_FILE,
    FILE_CONVERT,
    QUESTION_PACK_CREATE,
    QUESTION_PACK_EDIT,
    QUESTION_EDIT,
    NO_DATA;

    @StringRes fun getStringResource(): Int {
        return when (this) {
            UNKNOWN -> R.string.error_unknown
            NO_FILE -> R.string.error_file_convert
            FILE_CONVERT -> R.string.error_file_convert
            QUESTION_PACK_CREATE -> R.string.error_question_pack
            QUESTION_PACK_EDIT -> R.string.error_edit_question_pack
            QUESTION_EDIT -> R.string.error_question_edit
            NO_DATA -> R.string.error_no_data
        }
    }

}