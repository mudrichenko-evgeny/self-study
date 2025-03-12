package com.mudrichenkoevgeny.selfstudy.ui.adapter.statistics

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.ui.adapter.question.QuestionAdapterItemDecoration
import com.mudrichenkoevgeny.selfstudy.ui.base.adapter.BaseAdapter
import com.mudrichenkoevgeny.selfstudy.ui.base.adapter.BaseDiffUtilCallback

class StatisticsAdapter : BaseAdapter<StatisticsAdapterItemVM, StatisticsViewHolder>() {

    var marginBottom = 0
    var listener: StatisticsAdapterListener? = null

    override var diffUtilCallback: BaseDiffUtilCallback<StatisticsAdapterItemVM> =
        StatisticsAdapterDiffUtilCallback()

    override fun createViewHolderInstance(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        return StatisticsViewHolder(inflate(parent, R.layout.adapter_item_statistics))
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.binding.mainLayout.setOnClickListener {
            listener?.onItemClicked(item)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(QuestionAdapterItemDecoration(marginBottom))
        super.onAttachedToRecyclerView(recyclerView)
    }

}