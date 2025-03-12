package com.mudrichenkoevgeny.selfstudy.ui.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.mudrichenkoevgeny.selfstudy.BR
import com.mudrichenkoevgeny.selfstudy.data.SingleLiveEvent

abstract class BaseActivity<B: ViewDataBinding, VM : BaseActivityViewModel>
    : AppCompatActivity(), ActivityEventInterface {

    @get:LayoutRes
    abstract val layoutResId: Int

    protected lateinit var binding: B

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewBinding().let { viewBinding ->
            binding = viewBinding
            binding.setVariable(BR.viewModel, viewModel)
            binding.lifecycleOwner = this
        }
    }

    private fun getViewBinding(): B {
        return DataBindingUtil.setContentView(this, layoutResId)
    }

    override fun <T> LiveData<T>.observeData(f: (T) -> Unit) {
        this.observe(getActivity()) { data ->
            f.invoke(data)
        }
    }

    override fun <T> LiveData<SingleLiveEvent<T>>.observeSingleLiveEvent(f: (T) -> Unit) {
        this.observe(getActivity()) { data ->
            data.getContentIfNotHandled()?.let { f.invoke(it) }
        }
    }

    private fun getActivity() = this

}