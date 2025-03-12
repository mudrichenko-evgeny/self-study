package com.mudrichenkoevgeny.selfstudy.ui.base.bottom_sheet_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mudrichenkoevgeny.selfstudy.BR
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.data.SingleLiveEvent
import com.mudrichenkoevgeny.selfstudy.extensions.*
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.FragmentEventInterface
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBar
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarParentType

abstract class BaseBottomSheetDialog<B : ViewDataBinding, VM : BaseBottomSheetDialogViewModel> :
    BottomSheetDialogFragment(), FragmentEventInterface {

    @get:LayoutRes
    abstract val layoutResId: Int

    protected lateinit var binding: B

    protected abstract val viewModel: VM

    open fun onContentMaxHeightChanged(contentMaxHeight: Int) {
        viewModel.contentMaxHeight = contentMaxHeight
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        dialog?.window?.decorView?.let { decorView ->
            decorView.setOnApplyWindowInsetsListener { _, windowInsets ->
                expandDialog()
                calculateContentMaxHeight(windowInsets)
                windowInsets
            }
        }
    }

    private fun expandDialog() {
        dialog?.getBottomSheetBehaviour()?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun calculateContentMaxHeight(windowInsets: WindowInsets) {
        activity?.windowManager?.getBoundsHeight()?.let { boundsHeight ->
            onContentMaxHeightChanged(boundsHeight - windowInsets.getHeightInset())
        }
    }

    private fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): B {
        return DataBindingUtil.inflate(inflater, layoutResId, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.getBottomSheetBehaviour()?.skipCollapsed = true
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    override fun<T> LiveData<T>.observeData(f: (T) -> Unit) {
        this.observe(viewLifecycleOwner) { data ->
            f.invoke(data)
        }
    }

    override fun<T> LiveData<SingleLiveEvent<T>>.observeSingleLiveEvent(f: (T) -> Unit) {
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
                infoSnackBarParentType = InfoSnackBarParentType.BOTTOM_SHEET_DIALOG
            ).apply {
                show()
            }
        }
    }

}