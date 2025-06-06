package com.mudrichenkoevgeny.selfstudy.ui.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.mudrichenkoevgeny.selfstudy.BR

open class BaseViewHolder<Model, Binding : ViewDataBinding>(val binding: Binding)
    : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        initialized()
    }

    override val lifecycle: Lifecycle = lifecycleRegistry

    private fun initialized() {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun attachedToWindow() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun detachedFromWindow() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    open fun bind(model: Model) {
        binding.setVariable(BR.viewModel, model)
    }

}