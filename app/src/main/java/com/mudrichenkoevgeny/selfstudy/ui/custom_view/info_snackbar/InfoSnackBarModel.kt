package com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar

import androidx.annotation.StringRes

data class InfoSnackBarModel(
    val text: String? = null,
    @StringRes val textResId: Int? = null
)