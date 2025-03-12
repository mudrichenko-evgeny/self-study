package com.mudrichenkoevgeny.selfstudy.ui.screen.quiz

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.FragmentQuizBinding
import com.mudrichenkoevgeny.selfstudy.extensions.applyInsets
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.audio_recorder.AudioRecorderView
import com.mudrichenkoevgeny.selfstudy.ui.screen.base_audio_recorder.BaseAudioRecorderFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : BaseAudioRecorderFragment<FragmentQuizBinding, QuizViewModel>() {

    override val layoutResId = R.layout.fragment_quiz

    override val viewModel: QuizViewModel by viewModels()

    override val isBottomMenuVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainLayout.applyInsets(top = true, bottom = true)
        binding.answerEditText.addTextChangedListener { editable ->
            viewModel.onAnswerChanged(editable.toString())
        }
    }

    override fun getAudioRecorderView(): AudioRecorderView {
        return binding.audioRecorderView
    }

}