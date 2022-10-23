package com.mudrichenko.evgeniy.selfstudy.ui.adapter.question

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.ui.base.adapter.BaseAdapter
import com.mudrichenko.evgeniy.selfstudy.ui.base.adapter.BaseDiffUtilCallback

class QuestionAdapter : BaseAdapter<QuestionAdapterItemVM, QuestionViewHolder>() {

    var marginBottom = 0
    var listener: QuestionAdapterListener? = null

    override var diffUtilCallback: BaseDiffUtilCallback<QuestionAdapterItemVM> =
        QuestionAdapterDiffUtilCallback()

    override fun createViewHolderInstance(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(inflate(parent, R.layout.adapter_item_question))
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.binding.apply {
            editButton.setOnClickListener {
                listener?.onEditClicked(item)
            }
            mainLayout.setOnClickListener {
                listener?.onExpand(item)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(QuestionAdapterItemDecoration(marginBottom))
        super.onAttachedToRecyclerView(recyclerView)
    }

}