package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.pie_chart

import androidx.databinding.BindingAdapter

@BindingAdapter("sum", "progress", requireAll = true)
fun PieChartView.bindData(sum: Float, progress: Float) {
    setData(sum, progress)
}