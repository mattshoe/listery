package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mattshoe.shoebox.listery.authentication.login.ui.ErrorText
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.util.bottomBorder

@Composable
fun ListeryNumberInput(
    value: String?,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    highlightOnFocus: Boolean = true,
    textAlign: TextAlign = TextAlign.End,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
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
        onValueChange(it)
    }
}

@Composable
fun ListeryTextInput(
    value: EditableField<out Any?>,
    placeholder: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    highlightOnFocus: Boolean = true,
    password: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences
    ),
    leadingIcon: (@Composable (() -> Unit))? = null,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        ListeryTextInput(
            value.value?.toString() ?: "",
            placeholder,
            modifier,
            value.enabled,
            maxLines,
            highlightOnFocus,
            password,
            textAlign,
            keyboardOptions,
            leadingIcon,
            onValueChange
        )
        value.error?.let { errorMessage ->
            Spacer(modifier = Modifier.height(4.dp))
            ErrorText(
                text = errorMessage,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Composable
fun ListeryTextInput(
    value: String?,
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
    onValueChange: (String) -> Unit,
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value ?: "")) }
    if (value != textFieldValue.text)
        textFieldValue = textFieldValue.copy(text = value ?: "")

    ListeryTextInput(
        textFieldValue,
        placeholder,
        modifier,
        enabled,
        maxLines,
        highlightOnFocus,
        password,
        textAlign,
        keyboardOptions,
        leadingIcon
    ) {
        textFieldValue = it
        onValueChange(it.text)
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
    var textFieldValue by remember { mutableStateOf(value) }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    if (value.text != textFieldValue.text) {
        textFieldValue = textFieldValue.copy(text = value.text)
    }

    LaunchedEffect(isFocused) {
        if (isFocused && highlightOnFocus) {
            // Select all text when focused
            textFieldValue = value.copy(
                selection = TextRange(textFieldValue.text.length, 0)
            )
        }
    }

    BasicTextField(
        modifier = Modifier
            .bottomBorder(1.dp, Color.Gray)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .then(modifier),
        value = textFieldValue,
        onValueChange =  {
            textFieldValue = it
            onValueChange(it)
        },
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
                Box(
                    modifier = if (textAlign == TextAlign.End) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier.weight(1f)
                    }
                ) {
                    if (textFieldValue.text.isEmpty()) {
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
        value = "Sample text",
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