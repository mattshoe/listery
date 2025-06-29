package org.mattshoe.shoebox.listery.authentication.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.authentication.login.ui.ErrorText
import org.mattshoe.shoebox.listery.model.EditableField
import org.mattshoe.shoebox.listery.ui.common.ListeryTextInput

@Composable
fun PasswordInput(
    value: EditableField<String>,
    onValueChange: (String) ->  Unit,
) {

    ListeryTextInput(
        value = value,
        placeholder = "Password",
        password = true,
        onValueChange = onValueChange,
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