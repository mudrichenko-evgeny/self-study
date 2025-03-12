package com.mudrichenkoevgeny.selfstudy.ui.alert_dialog.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.DialogAlertBinding
import com.mudrichenkoevgeny.selfstudy.ui.base.alert_dialog.BaseAlertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertDialog: BaseAlertDialog<DialogAlertBinding, AlertDialogViewModel>() {

    override val layoutResId = R.layout.dialog_alert

    override val viewModel: AlertDialogViewModel by viewModels()

    private val args: AlertDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setContent(
            args.titleText,
            args.descriptionText,
            args.cancelButtonText,
            args.confirmButtonText,
            args.extraData
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
            sendConfirmationResult(false)
        }
        binding.confirmButton.setOnClickListener {
            findNavController().navigateUp()
            sendConfirmationResult(true)
        }
    }

    private fun sendConfirmationResult(confirmed: Boolean) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(
                RESULT_KEY to confirmed,
                RESULT_KEY_EXTRA_DATA to viewModel.extraData
            )
        )
    }

    companion object {
        private const val SCREEN_KEY = "confirmationDialog"
        const val REQUEST_KEY = "${SCREEN_KEY}RequestKey"
        const val RESULT_KEY = "resultKey"
        const val RESULT_KEY_EXTRA_DATA = "resultKeyExtraData"
    }

}