package com.mudrichenko.evgeniy.selfstudy.extensions

import android.app.Dialog
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun Dialog.getBottomSheetBehaviour(): BottomSheetBehavior<FrameLayout>? {
    findViewById<FrameLayout>(
        com.google.android.material.R.id.design_bottom_sheet
    )?.let { bottomSheetLayout ->
        return BottomSheetBehavior.from(bottomSheetLayout)
    }
    return null
}