package com.mudrichenko.evgeniy.selfstudy.ui.bottom_sheet_dialog.text_input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.databinding.DialogTextInputBinding
import com.mudrichenko.evgeniy.selfstudy.extensions.addOnDrawFinishedListener
import com.mudrichenko.evgeniy.selfstudy.ui.base.bottom_sheet_dialog.BaseBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextInputBottomSheetDialog: BaseBottomSheetDialog<DialogTextInputBinding, TextInputBottomSheetDialogViewModel>() {

    override val layoutResId = R.layout.dialog_text_input

    override val viewModel: TextInputBottomSheetDialogViewModel by viewModels()

    private val args: TextInputBottomSheetDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setContent(
            args.titleText,
            args.textContent,
            args.contentCanBeEmpty,
            args.hintText,
            args.confirmButtonText,
            args.extraData
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmButton.setOnClickListener {
            findNavController().navigateUp()
            sendTextInputResult(true)
        }
        binding.editText.addOnDrawFinishedListener {
            onContentMaxHeightChanged(viewModel.contentMaxHeight)
        }
    }

    override fun onContentMaxHeightChanged(contentMaxHeight: Int) {
        super.onContentMaxHeightChanged(contentMaxHeight)
        val currentContentHeight = binding.root.height
        val currentEditTextHeight = binding.editText.height
        val editTextMaxHeight = contentMaxHeight - currentContentHeight + currentEditTextHeight
        binding.editText.maxHeight = editTextMaxHeight
    }

    @Suppress("SameParameterValue")
    private fun sendTextInputResult(confirmed: Boolean) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(
                RESULT_KEY_CONFIRMED to confirmed,
                RESULT_KEY_TEXT to (viewModel.textContent.value ?: ""),
                RESULT_KEY_EXTRA_DATA to viewModel.extraData
            )
        )
    }

    companion object {
        private const val SCREEN_KEY = "textInputDialog"
        const val REQUEST_KEY = "${SCREEN_KEY}RequestKey"
        const val RESULT_KEY_CONFIRMED = "resultKeyConfirmed"
        const val RESULT_KEY_TEXT = "resultKeyText"
        const val RESULT_KEY_EXTRA_DATA = "resultKeyExtraData"
    }

}