package com.mudrichenkoevgeny.selfstudy.utils

import android.animation.AnimatorSet
import android.view.animation.OvershootInterpolator

fun animationSnackBar(): AnimatorSet {
    return AnimatorSet().apply {
        interpolator = OvershootInterpolator()
        duration = 500
    }
}