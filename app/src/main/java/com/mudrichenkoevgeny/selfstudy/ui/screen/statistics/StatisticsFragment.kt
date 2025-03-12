package com.mudrichenkoevgeny.selfstudy.ui.screen.statistics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.FragmentStatisticsBinding
import com.mudrichenkoevgeny.selfstudy.extensions.applyInsets
import com.mudrichenkoevgeny.selfstudy.extensions.getHeightWithMargin
import com.mudrichenkoevgeny.selfstudy.ui.adapter.statistics.StatisticsAdapter
import com.mudrichenkoevgeny.selfstudy.ui.alert_dialog.alert.AlertDialog
import com.mudrichenkoevgeny.selfstudy.ui.alert_dialog.alert.AlertDialogDirections
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding, StatisticsViewModel>() {

    override val layoutResId = R.layout.fragment_statistics

    override val viewModel: StatisticsViewModel by viewModels()

    private val statisticAdapter by lazy { StatisticsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainLayout.applyInsets(top = true)
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.clearStatisticsButton.setOnClickListener {
            showClearStatisticsDialog()
        }
        binding.questionsRecyclerView.apply {
            statisticAdapter.marginBottom = binding.clearStatisticsButton.getHeightWithMargin()
            statisticAdapter.listener = viewModel.statisticAdapterListener
            this.adapter = statisticAdapter
        }
        viewModel.onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(AlertDialog.REQUEST_KEY) { _, bundle ->
            bundle.getBoolean(AlertDialog.RESULT_KEY).let { clearStatisticsConfirmed ->
                if (clearStatisticsConfirmed) clearStatisticsConfirmed()
            }
        }
    }

    private fun showClearStatisticsDialog() {
        findNavController().navigate(
            AlertDialogDirections.showConfirmationDialog(
                titleText = getString(R.string.clear_statistics_warning),
                descriptionText = null,
                cancelButtonText = getString(R.string.cancel),
                confirmButtonText = getString(R.string.clear),
                extraData = null
            )
        )
    }

    private fun clearStatisticsConfirmed() {
        viewModel.clearStatisticsConfirmed()
    }

}