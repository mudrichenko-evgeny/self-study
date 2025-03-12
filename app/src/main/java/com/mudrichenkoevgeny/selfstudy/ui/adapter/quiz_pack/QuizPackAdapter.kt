package com.mudrichenkoevgeny.selfstudy.ui.adapter.quiz_pack

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.ui.adapter.question.QuestionAdapterItemDecoration
import com.mudrichenkoevgeny.selfstudy.ui.base.adapter.BaseAdapter
import com.mudrichenkoevgeny.selfstudy.ui.base.adapter.BaseDiffUtilCallback

class QuizPackAdapter : BaseAdapter<QuizPackAdapterItemVM, QuizPackViewHolder<out ViewDataBinding>>() {

    var marginBottom = 0
    var listener: QuizPackAdapterListener? = null

    override var diffUtilCallback: BaseDiffUtilCallback<QuizPackAdapterItemVM> =
        QuizPackAdapterDiffUtilCallback()

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getAdapterItemType()
    }

    override fun createViewHolderInstance(
        parent: ViewGroup,
        viewType: Int
    ): QuizPackViewHolder<out ViewDataBinding> {
        return when(viewType) {
            QuizPackAdapterItemVM.QUIZ_PACK_ACTIVE -> {
                QuizPackViewHolderActiveItem(
                    inflate(parent, R.layout.adapter_item_quiz_pack_active)
                )
            }
            QuizPackAdapterItemVM.QUIZ_PACK -> {
                QuizPackViewHolderItem(
                    inflate(parent, R.layout.adapter_item_quiz_pack)
                )
            }
            else -> {
                QuizPackViewHolderHelpItem(
                    inflate(parent, R.layout.adapter_item_help)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: QuizPackViewHolder<out ViewDataBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        when (holder) {
            is QuizPackViewHolderActiveItem -> {
                holder.binding.editButton.setOnClickListener {
                    listener?.onEditClicked(item)
                }
                holder.binding.statisticsButton.setOnClickListener {
                    listener?.onStatisticsClicked(item)
                }
            }
            is QuizPackViewHolderItem -> {
                holder.binding.deleteButton.setOnClickListener {
                    if (item is QuizPackAdapterItemVM.QuizPackItem)
                        listener?.onDeleteClicked(item.quizPack)
                }
                holder.binding.mainLayout.setOnClickListener {
                    if (item is QuizPackAdapterItemVM.QuizPackItem)
                        listener?.onSetActiveClicked(item.quizPack)
                }
            }
            is QuizPackViewHolderHelpItem -> {
                holder.binding.image.setOnClickListener {  }
                holder.binding.mainLayout.setOnClickListener {
                    listener?.onHelpExpand()
                }
                holder.binding.helpButton.setOnClickListener {
                    listener?.onHelpExpand()
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(QuestionAdapterItemDecoration(marginBottom))
        super.onAttachedToRecyclerView(recyclerView)
    }

}