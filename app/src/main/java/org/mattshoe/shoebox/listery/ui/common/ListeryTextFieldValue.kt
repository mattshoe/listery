package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun ListeryTextFieldValue(
    value: String?,
    cursorPosition: Int = Int.MAX_VALUE
): TextFieldValue = TextFieldValue(value ?: "", TextRange(cursorPosition))