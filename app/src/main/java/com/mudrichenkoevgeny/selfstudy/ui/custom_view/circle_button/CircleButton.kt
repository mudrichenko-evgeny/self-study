package com.mudrichenkoevgeny.selfstudy.ui.custom_view.circle_button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorOrThrow
import com.mudrichenkoevgeny.selfstudy.R

class CircleButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageButton(context, attrs) {

    private var enabledBackgroundColor: Int = getDefaultEnabledBackgroundColor()
    private var disabledBackgroundColor: Int = getDefaultDisabledBackgroundColor()
    private var enabledImageColor: Int? = null
    private var disabledImageColor: Int? = null

    private var isButtonEnabled: Boolean = isEnabled

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        background = getDefaultBackgroundColor()
        foreground = getDefaultForegroundColor()
        context.obtainStyledAttributes(attrs, R.styleable.CircleButton).apply {
            enabledBackgroundColor = getColor(
                R.styleable.CircleButton_enabledBackgroundColorRes,
                getDefaultEnabledBackgroundColor()
            )
            disabledBackgroundColor = getColor(
                R.styleable.CircleButton_disabledBackgroundColorRes,
                getDefaultDisabledBackgroundColor()
            )
            try {
                enabledImageColor = getColorOrThrow(R.styleable.CircleButton_enabledImageColorRes)
                disabledImageColor = getColorOrThrow(R.styleable.CircleButton_disabledImageColorRes)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            setColors()
            recycle()
        }
    }

    fun setColors(
        enabledBackgroundColor: Int?,
        disabledBackgroundColor: Int?,
        enabledImageColor: Int?,
        disabledImageColor: Int?
    ) {
        enabledBackgroundColor?.let { this.enabledBackgroundColor = it }
        disabledBackgroundColor?.let { this.disabledBackgroundColor = it }
        enabledImageColor?.let { this.enabledImageColor = it }
        disabledImageColor?.let { this.disabledImageColor = it }
        setColors()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        (w / 4).let { padding ->
            setPadding(padding, padding, padding, padding)
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun setEnabled(isEnabled: Boolean) {
        isButtonEnabled = isEnabled
        isClickable = isEnabled
        setColors()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setImageTint(enabledImageColor, disabledImageColor)
    }

    private fun setColors() {
        setImageTint(enabledImageColor, disabledImageColor)
        background.setTint(if (isButtonEnabled) enabledBackgroundColor else disabledBackgroundColor)
    }

    private fun getDefaultEnabledBackgroundColor(): Int {
        return ContextCompat.getColor(context, R.color.background_primary)
    }

    private fun getDefaultDisabledBackgroundColor(): Int {
        return ContextCompat.getColor(context, R.color.background_secondary)
    }

    private fun setImageTint(
        enabledImageColor: Int?,
        disabledImageColor: Int?
    ) {
        if (enabledImageColor == null || disabledImageColor == null) return
        drawable?.setTint(if (isButtonEnabled) enabledImageColor else disabledImageColor)
    }

    private fun getDefaultBackgroundColor(): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.background_circle)
    }

    private fun getDefaultForegroundColor(): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.foreground_ripple_circle)
    }

}