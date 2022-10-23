package com.mudrichenko.evgeniy.selfstudy.ui.screen.quiz_completed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.databinding.FragmentQuizCompletedBinding
import com.mudrichenko.evgeniy.selfstudy.extensions.applyInsets
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizCompletedFragment : BaseFragment<FragmentQuizCompletedBinding, QuizCompletedViewModel>() {

    override val layoutResId = R.layout.fragment_quiz_completed

    override val viewModel: QuizCompletedViewModel by viewModels()

    override val isBottomMenuVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainLayout.applyInsets(top = true, bottom = true)
    }

}