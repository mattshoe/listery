package org.mattshoe.shoebox.listery.authentication.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun PasswordInput(
    value: String,
    onValueChange: (String) ->  Unit,
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }

    if (value != textFieldValue.text)
        textFieldValue = textFieldValue.copy(text = value)

    ListeryTextInput(
        value = textFieldValue,
        placeholder = "Password",
        password = true,
        onValueChange = {
            textFieldValue = it
            onValueChange(it.text)
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = "Password Icon",
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outline)
            )
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password
        )
    )
}