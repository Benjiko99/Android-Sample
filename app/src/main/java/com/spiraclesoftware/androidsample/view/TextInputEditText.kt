package com.spiraclesoftware.androidsample.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.textfield.TextInputEditText as MaterialTextInputEditText

class TextInputEditText : MaterialTextInputEditText {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.TextInputEditText,
            defStyleAttr,
            R.style.Widget_Design_TextInputEditText
        ).use { initStyledAttributes(it) }
    }

    private fun initStyledAttributes(attrs: TypedArray) {
    }

}