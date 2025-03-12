package com.mudrichenkoevgeny.selfstudy.ui.custom_view.number_picker

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.mudrichenkoevgeny.selfstudy.databinding.ViewNumberPickerBinding
import com.mudrichenkoevgeny.selfstudy.system.InputFilterIntRange

class NumberPickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var listener: Listener? = null
    private lateinit var binding: ViewNumberPickerBinding

    private var textWatcher: TextWatcher? = null
    private var textSelectionStart: Int = 0
    private var textSelectionEnd: Int = 0

    private var number: Int = 0
    private var minNumber: Int = 1
    private var maxNumber: Int = 1

    init {
        initView(context, attrs)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun initView(context: Context, attrs: AttributeSet?) {
        binding = ViewNumberPickerBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        binding.questionCountMinus.setOnClickListener {
            onNumberMinusClicked()
        }
        binding.questionCountPlus.setOnClickListener {
            onNumberPlusClicked()
        }
        addEditTextListener()
    }

    private fun addEditTextListener() {
        binding.questionCountEditText.setSelection(textSelectionStart, textSelectionEnd)
        textWatcher = binding.questionCountEditText.addTextChangedListener { editable ->
            editable?.toString()?.toIntOrNull()?.let { number ->
                if (number in minNumber..maxNumber) {
                    onValueChanged(number)
                }
            }
        }
    }

    private fun removeEditTextListener() {
        textSelectionStart = binding.questionCountEditText.selectionStart
        textSelectionEnd = binding.questionCountEditText.selectionEnd
        textWatcher?.let { textWatcher ->
            binding.questionCountEditText.removeTextChangedListener(textWatcher)
        }
    }

    private fun onNumberMinusClicked() {
        if (minNumber < number) {
            onValueChanged(number - 1)
        }
    }

    private fun onNumberPlusClicked() {
        if (maxNumber > number) {
            onValueChanged(number + 1)
        }
    }

    private fun onValueChanged(number: Int, listenerEnabled: Boolean = true) {
        removeEditTextListener()
        this.number = number
        binding.questionCountEditText.setText("$number")
        binding.questionCountMinus.isEnabled = number > minNumber
        binding.questionCountPlus.isEnabled = number < maxNumber
        if (listenerEnabled) listener?.onNumberChanged(number)
        addEditTextListener()
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun setNumber(number: Int?) {
        number?.let {
            onValueChanged(
                number = number,
                listenerEnabled = false)
        }
    }

    fun setMinNumber(minNumber: Int?) {
        this.minNumber = minNumber ?: 1
        updateEditTextFilters()
    }

    fun setMaxNumber(maxNumber: Int?) {
        this.maxNumber = maxNumber ?: 1
        updateEditTextFilters()
    }

    private fun updateEditTextFilters() {
        binding.questionCountEditText.filters = arrayOf(
            InputFilterIntRange(minNumber, maxNumber)
        )
    }

    interface Listener {
        fun onNumberChanged(number: Int)
    }

}