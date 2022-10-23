package com.mudrichenko.evgeniy.selfstudy.ui.binding_adapter

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mudrichenko.evgeniy.selfstudy.extensions.setVisibleOrGone
import com.mudrichenko.evgeniy.selfstudy.extensions.setVisibleOrInvisible
import com.mudrichenko.evgeniy.selfstudy.ui.base.adapter.BaseAdapter

@BindingAdapter("items")
fun <T> RecyclerView.bindItems(items: MutableList<T>?) {
    items?.let {
        @Suppress("UNCHECKED_CAST")
        try {
            if (adapter != null && adapter is BaseAdapter<*, *>) {
                (adapter as BaseAdapter<T, *>).updateItems(it)
            }
        } catch (t: Throwable) {
            throw IllegalStateException("Incoming adapter is an incompatible type")
        }
    }
}

@BindingAdapter("visibleOrGone")
fun View.bindVisibleOrGone(visible: Boolean?) {
    setVisibleOrGone(visible == true)
}

@Suppress("unused")
@BindingAdapter("visibleOrInvisible")
fun View.bindVisibleOrInvisible(visible: Boolean?) {
    setVisibleOrInvisible(visible == true)
}

@Suppress("unused")
@BindingAdapter("enabled", "enabledTintColorRes", "disabledTintColorRes", requireAll = true)
fun View.setEnabledTint(
    enabled: Boolean,
    @ColorRes enabledColorRes: Int,
    @ColorRes disabledColorRes: Int
) {
    this.background.setTint(
        ContextCompat.getColor(context, if (enabled) enabledColorRes else disabledColorRes)
    )
}