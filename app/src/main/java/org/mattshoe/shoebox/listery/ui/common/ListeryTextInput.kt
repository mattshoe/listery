package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mattshoe.shoebox.listery.util.bottomBorder

@Composable
fun ListeryNumberInput(
    value: TextFieldValue,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullBorder: Boolean = false,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.End,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (TextFieldValue) -> Unit
) {
    ListeryTextInput(
        value,
        placeholder,
        modifier,
        enabled,
        fullBorder,
        maxLines,
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
    fullBorder: Boolean = false,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Start,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences
    ),
    onValueChange: (TextFieldValue) -> Unit
) {
    BasicTextField(
        modifier = Modifier
            .widthIn(min = 48.dp)
            .bottomBorder(1.dp, Color.Gray)
            .apply {
                if (fullBorder)
                    bottomBorder(1.dp, Color.Gray)
            }
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