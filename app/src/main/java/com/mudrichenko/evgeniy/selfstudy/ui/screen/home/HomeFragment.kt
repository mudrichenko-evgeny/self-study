package com.mudrichenko.evgeniy.selfstudy.ui.screen.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.databinding.FragmentHomeBinding
import com.mudrichenko.evgeniy.selfstudy.extensions.applyInsets
import com.mudrichenko.evgeniy.selfstudy.ui.alert_dialog.alert.AlertDialog
import com.mudrichenko.evgeniy.selfstudy.ui.alert_dialog.alert.AlertDialogDirections
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val layoutResId = R.layout.fragment_home

    override val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainLayout.applyInsets(top = true)
        binding.finishQuizButton.setOnClickListener {
            showFinishQuizDialog()
        }
        binding.helpButton.setOnClickListener {
            showHelpDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(AlertDialog.REQUEST_KEY) { _, bundle ->
            bundle.getBoolean(AlertDialog.RESULT_KEY).let { finishQuizConfirmed ->
                if (finishQuizConfirmed) finishQuizConfirmed()
            }
        }
    }

    private fun showFinishQuizDialog() {
        findNavController().navigate(
            AlertDialogDirections.showConfirmationDialog(
                titleText = getString(R.string.finish_quiz_title),
                descriptionText = getString(R.string.finish_quiz_description),
                cancelButtonText = getString(R.string.cancel),
                confirmButtonText = getString(R.string.finish),
                extraData = null
            )
        )
    }

    private fun showHelpDialog() {
        findNavController().navigate(
            AlertDialogDirections.showConfirmationDialog(
                titleText = getString(R.string.help_quiz_title),
                descriptionText = getString(R.string.help_quiz_description),
                cancelButtonText = getString(R.string.close),
                confirmButtonText = null,
                extraData = null
            )
        )
    }

    private fun finishQuizConfirmed() {
        viewModel.finishQuizConfirmed()
    }

}

