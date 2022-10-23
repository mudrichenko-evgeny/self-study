package com.mudrichenko.evgeniy.selfstudy.ui.screen.question_pack_edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.databinding.FragmentQuestionPackEditBinding
import com.mudrichenko.evgeniy.selfstudy.extensions.applyInsets
import com.mudrichenko.evgeniy.selfstudy.extensions.getHeightWithMargin
import com.mudrichenko.evgeniy.selfstudy.ui.adapter.question.QuestionAdapter
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragment
import com.mudrichenko.evgeniy.selfstudy.ui.bottom_sheet_dialog.text_input.TextInputBottomSheetDialog
import com.mudrichenko.evgeniy.selfstudy.ui.bottom_sheet_dialog.text_input.TextInputBottomSheetDialogDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionPackEditFragment : BaseFragment<FragmentQuestionPackEditBinding, QuestionPackEditViewModel>() {

    override val layoutResId = R.layout.fragment_question_pack_edit

    override val viewModel: QuestionPackEditViewModel by viewModels()

    private val questionAdapter by lazy { QuestionAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editNameButton.setOnClickListener {
            showNameInputDialog()
        }
        binding.mainLayout.applyInsets(top = true)
        binding.questionsRecyclerView.apply {
            questionAdapter.marginBottom = binding.createQuestionButton.getHeightWithMargin()
            questionAdapter.listener = viewModel.questionAdapterListener
            this.adapter = questionAdapter
        }
    }

    private fun showNameInputDialog() {
        findNavController().navigate(
            TextInputBottomSheetDialogDirections.showTextInputDialog(
                titleText = getString(R.string.enter_pack_name),
                textContent = binding.titleTextView.text.toString(),
                contentCanBeEmpty = false,
                hintText = getString(R.string.pack_name),
                confirmButtonText = getString(R.string.set_name),
                extraData = null
            )
        )
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(TextInputBottomSheetDialog.REQUEST_KEY) { _, bundle ->
            bundle.getBoolean(TextInputBottomSheetDialog.RESULT_KEY_CONFIRMED).let { textInputConfirmed ->
                if (textInputConfirmed) {
                    viewModel.onPackNameChanged(
                        packName = bundle.getString(TextInputBottomSheetDialog.RESULT_KEY_TEXT)
                    )
                }
            }
        }
    }

}
