package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.switch_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.res.getColorOrThrow
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.databinding.ViewSwitchBinding
import com.mudrichenko.evgeniy.selfstudy.utils.colorStateListChecked

class SwitchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private lateinit var binding: ViewSwitchBinding

    var listener: Listener? = null

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        binding = ViewSwitchBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        binding.mainLayout.setOnClickListener {
            binding.switchView.performClick()
        }
        binding.switchView.setOnCheckedChangeListener { _, b ->
            listener?.onCheckChanged(b)
        }
        context.obtainStyledAttributes(attrs, R.styleable.SwitchView).apply {
            try {
                setColors(
                    accentColor = getColorOrThrow(
                        R.styleable.SwitchView_accentColor
                    ),
                    primaryColor = getColorOrThrow(
                        R.styleable.SwitchView_primaryColor
                    ),
                    secondaryColor = getColorOrThrow(
                        R.styleable.SwitchView_secondaryColor
                    ),
                    trackColor = getColorOrThrow(
                        R.styleable.SwitchView_trackColor
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            recycle()
        }
    }

    private fun setColors(
        accentColor: Int,
        primaryColor: Int,
        secondaryColor: Int,
        trackColor: Int
    ) {
        binding.mainLayout.background?.setTint(primaryColor)
        binding.switchView.apply {
            thumbTintList = colorStateListChecked(
                secondaryColor, accentColor
            )
            trackTintList = colorStateListChecked(
                trackColor, trackColor
            )
        }
        binding.switchView.setTextColor(accentColor)
    }

    fun setText(text: String) {
        binding.switchView.text = text
    }

    interface Listener {
        fun onCheckChanged(checked: Boolean)
    }

}