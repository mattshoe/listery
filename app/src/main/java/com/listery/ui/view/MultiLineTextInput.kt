package com.listery.ui.view

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.EditText
import com.listery.R

open class MultiLineTextInput : androidx.appcompat.widget.AppCompatEditText {

    constructor(context: Context) : super(context) { initialize(null, 0) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { initialize(attrs, 0) }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) { initialize(attrs, defStyle) }
    private fun initialize(attrs: AttributeSet?, defStyle: Int) = init(attrs, defStyle)

    protected open fun init(attrs: AttributeSet?, defStyle: Int) {
        maxLines = 4
        movementMethod = ScrollingMovementMethod()
        if (imeOptions == 0) {
            imeOptions = IME_ACTION_NEXT
        }
    }
}