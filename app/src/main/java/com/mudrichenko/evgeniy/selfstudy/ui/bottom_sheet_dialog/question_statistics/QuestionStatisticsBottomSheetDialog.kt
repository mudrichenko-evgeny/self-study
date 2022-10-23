package com.mudrichenko.evgeniy.selfstudy.ui.bottom_sheet_dialog.question_statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.databinding.DialogQuestionStatisticsBinding
import com.mudrichenko.evgeniy.selfstudy.extensions.addOnDrawFinishedListener
import com.mudrichenko.evgeniy.selfstudy.extensions.getHeightForAllViewWithExclude
import com.mudrichenko.evgeniy.selfstudy.ui.base.bottom_sheet_dialog.BaseBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionStatisticsBottomSheetDialog:
    BaseBottomSheetDialog<DialogQuestionStatisticsBinding,
            QuestionStatisticsBottomSheetDialogViewModel>()
{

    override val layoutResId = R.layout.dialog_question_statistics

    override val viewModel: QuestionStatisticsBottomSheetDialogViewModel by viewModels()

    private val args: QuestionStatisticsBottomSheetDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setQuestionId(args.questionId)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.contentTextView.addOnDrawFinishedListener {
            onContentMaxHeightChanged(viewModel.contentMaxHeight)
        }
    }

    override fun onContentMaxHeightChanged(contentMaxHeight: Int) {
        super.onContentMaxHeightChanged(contentMaxHeight)
        val scrollMaxHeight = contentMaxHeight - binding.mainLayout.getHeightForAllViewWithExclude(
            listOf(binding.contentScrollView.id)
        )
        binding.contentScrollView.addOnDrawFinishedListener {
            if (binding.contentScrollView.height > scrollMaxHeight) {
                (binding.contentScrollView.layoutParams as ConstraintLayout.LayoutParams).let { layoutParams ->
                    layoutParams.height = scrollMaxHeight
                    binding.contentScrollView.layoutParams = layoutParams
                }
            }
        }
    }

}