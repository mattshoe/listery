package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mattshoe.shoebox.listery.util.bottomBorder

@Composable
fun ListeryNumberInput(
    value: TextFieldValue,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    highlightOnFocus: Boolean = true,
    textAlign: TextAlign = TextAlign.End,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (TextFieldValue) -> Unit
) {
    ListeryTextInput(
        value,
        placeholder,
        modifier,
        enabled,
        1,
        highlightOnFocus,
        textAlign,
        keyboardOptions.copy(
            keyboardType = KeyboardType.Number
        )
    ) {
        onValueChange(
            it.copy(
                text = it.text.trim().removeLeadingZeros()
            )
        )
    }
}

@Composable
fun ListeryTextInput(
    value: TextFieldValue,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    maxLines: Int = 1,
    highlightOnFocus: Boolean = true,
    textAlign: TextAlign = TextAlign.Start,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences
    ),
    onValueChange: (TextFieldValue) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    
    LaunchedEffect(isFocused) {
        if (isFocused && value.text.isNotEmpty() && highlightOnFocus) {
            // Select all text when focused
            onValueChange(
                value.copy(
                    selection = androidx.compose.ui.text.TextRange(value.text.length, 0)
                )
            )
        }
    }
    
    BasicTextField(
        modifier = Modifier
            .bottomBorder(1.dp, Color.Gray)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .then(modifier),
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.labelLarge.copy(
            color = MaterialTheme.colorScheme.outline,
            lineHeight = 26.sp,
            textAlign = textAlign
        ),
        keyboardOptions = keyboardOptions,
        singleLine = maxLines == 1,
        maxLines = maxLines,
        enabled = enabled,
        decorationBox = { innerTextField ->
            if (value.text.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = textAlign
                    )
                )
            }
            innerTextField()
        }
    )
}

private fun String.removeLeadingZeros(): String {
    return if (isEmpty()) this
    else if (startsWith("-")) "-" + substring(1).removeLeadingZeros()
    else trimStart('0').ifEmpty { "0" }
}