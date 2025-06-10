package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mattshoe.shoebox.listery.util.bottomBorder

@Composable
fun ListeryTextInput(
    value: TextFieldValue,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fullBorder: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences
    ),
    onValueChange: (TextFieldValue) -> Unit
) {
    BasicTextField(
        modifier = Modifier
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
            lineHeight = 26.sp
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true,
        enabled = enabled,
        decorationBox = { innerTextField ->
            if (value.text.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.outline
                    )
                )
            }
            innerTextField()
        }
    )
}