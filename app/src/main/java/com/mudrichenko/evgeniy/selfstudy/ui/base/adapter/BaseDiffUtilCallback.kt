package com.mudrichenko.evgeniy.selfstudy.ui.base.adapter

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtilCallback<Model> : DiffUtil.Callback() {

    private var oldList: List<Model> = emptyList()
    private var newList: List<Model> = emptyList()

    abstract fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean
    abstract fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    fun setLists(oldList: List<Model>, newList: List<Model>) {
        this.oldList = oldList
        this.newList = newList
    }

}