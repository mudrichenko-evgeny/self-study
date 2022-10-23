package com.mudrichenko.evgeniy.selfstudy.ui.adapter.statistics

import com.mudrichenko.evgeniy.selfstudy.ui.base.adapter.BaseDiffUtilCallback

class StatisticsAdapterDiffUtilCallback : BaseDiffUtilCallback<StatisticsAdapterItemVM>() {

    override fun areItemsTheSame(
        oldItem: StatisticsAdapterItemVM,
        newItem: StatisticsAdapterItemVM
    ): Boolean {
        return oldItem.question.id == oldItem.question.id
    }

    override fun areContentsTheSame(
        oldItem: StatisticsAdapterItemVM,
        newItem: StatisticsAdapterItemVM
    ): Boolean {
        return oldItem == newItem
    }

}