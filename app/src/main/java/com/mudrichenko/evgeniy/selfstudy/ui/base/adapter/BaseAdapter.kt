package com.mudrichenko.evgeniy.selfstudy.ui.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<Model, ViewHolder: BaseViewHolder<Model, out ViewDataBinding>>
    : RecyclerView.Adapter<ViewHolder>() {

    private var items: List<Model> = mutableListOf()

    abstract var diffUtilCallback: BaseDiffUtilCallback<Model>

    abstract fun createViewHolderInstance(parent: ViewGroup, viewType: Int): ViewHolder

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.detachedFromWindow()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return createViewHolderInstance(parent, viewType).apply {
            binding.lifecycleOwner = this
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun getItem(position: Int): Model {
        return items[position]
    }

    fun getItems(): List<Model> = items

    fun <Binding : ViewDataBinding> inflate(parent: ViewGroup, layoutResId: Int): Binding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutResId,
            parent,
            false
        )

    open fun updateItems(newItems: List<Model>) {
        DiffUtil.calculateDiff(diffUtilCallback.apply {
            setLists(items, newItems)
        }).also {
            updateList(newItems)
            it.dispatchUpdatesTo(this)
        }
    }

    protected fun updateList(newItems: List<Model>) {
        items = newItems
    }

}