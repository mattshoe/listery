package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

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
        false,
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
    password: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences
    ),
    leadingIcon: (@Composable (() -> Unit))? = null,
    onValueChange: (TextFieldValue) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    
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
        visualTransformation = if (!password || passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        decorationBox = { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Box(Modifier.weight(1f)) {
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
                if (password) {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    // Please provide localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                }
            }
        }
    )
}

private fun String.removeLeadingZeros(): String {
    return if (isEmpty()) this
    else if (startsWith("-")) "-" + substring(1).removeLeadingZeros()
    else trimStart('0').ifEmpty { "0" }
}

@Preview(name = "TextInput with Icon", showBackground = true)
@Composable
private fun PreviewListeryTextInputWithIcon() {
    ListeryTextInput(
        value = TextFieldValue(""),
        placeholder = "Email",
        onValueChange = {},
        leadingIcon = {
            Box(
                Modifier
                    .size(20.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(4.dp))
            )
        }
    )
}