package com.mudrichenkoevgeny.selfstudy.ui.base.alert_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import com.mudrichenkoevgeny.selfstudy.BR
import com.mudrichenkoevgeny.selfstudy.data.SingleLiveEvent
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.FragmentEventInterface
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBar
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarParentType

abstract class BaseAlertDialog <B : ViewDataBinding, VM : BaseAlertDialogViewModel> :
    DialogFragment(), FragmentEventInterface {

    @get:LayoutRes
    abstract val layoutResId: Int

    protected lateinit var binding: B

    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        getViewBinding(inflater, container).let { viewBinding ->
            binding = viewBinding
            binding.setVariable(BR.viewModel, viewModel)
            binding.lifecycleOwner = viewLifecycleOwner
            return viewBinding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dismissEvent.observeSingleLiveEvent {
            this.dismiss()
        }
        viewModel.showSnackBarEvent.observeSingleLiveEvent { infoSnackBarModel ->
            this.showInfoSnackBar(infoSnackBarModel)
        }
    }

    private fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): B {
        return DataBindingUtil.inflate(inflater, layoutResId, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun <T> LiveData<T>.observeData(f: (T) -> Unit) {
        this.observe(viewLifecycleOwner) { data ->
            f.invoke(data)
        }
    }

    override fun <T> LiveData<SingleLiveEvent<T>>.observeSingleLiveEvent(f: (T) -> Unit) {
        this.observe(viewLifecycleOwner) { data ->
            data.getContentIfNotHandled()?.let { f.invoke(it) }
        }
    }

    override fun showInfoSnackBar(infoSnackBarModel: InfoSnackBarModel) {
        dialog?.window?.decorView?.let { containerView ->
            var coordinatorLayout: CoordinatorLayout? = null
            if (containerView is ViewGroup) {
                coordinatorLayout = CoordinatorLayout(requireContext())
                containerView.addView(coordinatorLayout)
            }
            InfoSnackBar.make(
                containerView = coordinatorLayout ?: containerView,
                infoSnackBarModel = infoSnackBarModel,
                infoSnackBarParentType = InfoSnackBarParentType.ALERT_DIALOG
            ).apply {
                show()
            }
        }
    }

}