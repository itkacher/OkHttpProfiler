package io.nerdythings.modifier.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun TextInputField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1,
    hint: String = "",
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        label = label,
        value = text,
        onValueChange = onTextChange,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        placeholder = { Text(hint) },
        trailingIcon = {
            if (text.isNotEmpty() && enabled) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                    modifier =
                    Modifier.clickable {
                        onTextChange("")
                    },
                )
            }
        },
        maxLines = maxLines,
    )
}

@Composable
internal fun TextArea(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    hint: String = "",
    maxLines: Int = Int.MAX_VALUE,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        label = label,
        enabled = enabled,
        onValueChange = onTextChange,
        placeholder = { Text(hint) },
        trailingIcon = {
            if (text.isNotEmpty() && enabled) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                    modifier =
                    Modifier.clickable {
                        onTextChange("")
                    },
                )
            }
        },
        minLines = 5,
        maxLines = maxLines,
    )
}
