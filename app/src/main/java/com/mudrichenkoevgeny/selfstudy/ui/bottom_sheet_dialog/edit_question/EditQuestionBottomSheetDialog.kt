package com.mudrichenkoevgeny.selfstudy.ui.bottom_sheet_dialog.edit_question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.DialogEditQuestionBinding
import com.mudrichenkoevgeny.selfstudy.extensions.addOnDrawFinishedListener
import com.mudrichenkoevgeny.selfstudy.ui.base.bottom_sheet_dialog.BaseBottomSheetDialog
import com.mudrichenkoevgeny.selfstudy.ui.screen.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditQuestionBottomSheetDialog: BaseBottomSheetDialog<DialogEditQuestionBinding, EditQuestionBottomSheetDialogViewModel>() {

    override val layoutResId = R.layout.dialog_edit_question

    override val viewModel: EditQuestionBottomSheetDialogViewModel by viewModels()

    private val mainViewModel: MainActivityViewModel by activityViewModels()

    private val args: EditQuestionBottomSheetDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setData(args.quizPackId, args.questionId)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.editSuccessfulEvent.observeSingleLiveEvent { quizPack ->
            quizPack?.let { mainViewModel.editQuiz(quizPack) }
            dismiss()
        }
        binding.contentEditText.addOnDrawFinishedListener {
            onContentMaxHeightChanged(viewModel.contentMaxHeight)
        }
    }

    override fun onContentMaxHeightChanged(contentMaxHeight: Int) {
        super.onContentMaxHeightChanged(contentMaxHeight)
        val currentContentHeight = binding.root.height
        val currentEditTextsHeight = binding.contentEditText.height + binding.answerEditText.height
        val editTextMaxHeight = (contentMaxHeight - currentContentHeight + currentEditTextsHeight) / 2
        binding.contentEditText.maxHeight = editTextMaxHeight
        binding.answerEditText.maxHeight = editTextMaxHeight
    }

}