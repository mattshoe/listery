package com.listery.ui.view

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import com.listery.R

class SingleLineDecimalInput : SingleLineTextInput {


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun init(attrs: AttributeSet?, defStyle: Int) {
        super.init(attrs, defStyle)
        inputType = InputType.TYPE_CLASS_NUMBER
        if (maxLines == 0) {
            maxLines = 4
        }
    }
}