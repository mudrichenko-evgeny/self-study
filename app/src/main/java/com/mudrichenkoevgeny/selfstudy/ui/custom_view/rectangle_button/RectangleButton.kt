package com.mudrichenkoevgeny.selfstudy.ui.custom_view.rectangle_button

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorOrThrow
import com.mudrichenkoevgeny.selfstudy.R

class RectangleButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {

    private var enabledBackgroundColor: Int = getDefaultEnabledBackgroundColor()
    private var disabledBackgroundColor: Int = getDefaultDisabledBackgroundColor()
    private var enabledTextColor: Int? = null
    private var disabledTextColor: Int? = null

    private var isButtonEnabled: Boolean = isEnabled

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.RectangleButton).apply {
            enabledBackgroundColor = getColor(
                R.styleable.RectangleButton_enabledBackgroundColorRes,
                getDefaultEnabledBackgroundColor()
            )
            disabledBackgroundColor = getColor(
                R.styleable.RectangleButton_disabledBackgroundColorRes,
                getDefaultDisabledBackgroundColor()
            )
            try {
                enabledTextColor = getColorOrThrow(R.styleable.RectangleButton_enabledTextColorRes)
                disabledTextColor = getColorOrThrow(R.styleable.RectangleButton_disabledTextColorRes)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            setColors()
            recycle()
        }
    }

    override fun setEnabled(isEnabled: Boolean) {
        super.setEnabled(isEnabled)
        isButtonEnabled = isEnabled
        setColors()
    }

    private fun setColors() {
        setTextColor(enabledTextColor, disabledTextColor)
        background.setTint(if (isButtonEnabled) enabledBackgroundColor else disabledBackgroundColor)
    }

    private fun getDefaultEnabledBackgroundColor(): Int {
        return ContextCompat.getColor(context, R.color.background_primary)
    }

    private fun getDefaultDisabledBackgroundColor(): Int {
        return ContextCompat.getColor(context, R.color.background_secondary)
    }

    private fun setTextColor(
        enabledTextColor: Int?,
        disabledTextColor: Int?
    ) {
        if (enabledTextColor == null || disabledTextColor == null) return
        setTextColor(
            if (isButtonEnabled) enabledTextColor else disabledTextColor
        )
    }

}