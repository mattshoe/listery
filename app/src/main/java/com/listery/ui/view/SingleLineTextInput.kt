package com.listery.ui.view

import android.content.Context
import android.graphics.Canvas
import android.text.InputType
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT

open class SingleLineTextInput : androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    constructor(context: Context) : super(context) { initialize(null, 0) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { initialize(attrs, 0) }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) { initialize(attrs, defStyle) }
    private fun initialize(attrs: AttributeSet?, defStyle: Int) = init(attrs, defStyle)

    protected open fun init(attrs: AttributeSet?, defStyle: Int) {
        this.setLines(1)
        this.inputType = InputType.TYPE_CLASS_TEXT
        if (imeOptions == 0) {
            imeOptions = IME_ACTION_NEXT
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}