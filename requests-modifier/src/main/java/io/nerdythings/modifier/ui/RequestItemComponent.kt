package io.nerdythings.modifier.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.nerdythings.modifier.data.CustomResponse
import io.nerdythings.requests.modifier.R

@Composable
internal fun RequestItem(
    path: String,
    customResponse: CustomResponse,
    modifier: Modifier = Modifier,
    onSaveResponse: (CustomResponse) -> Unit,
    onRemoveCustomResponse: (String) -> Unit,
) {
    val url = remember { mutableStateOf(path) }
    val response = remember { mutableStateOf(customResponse.response) }
    val code = remember { mutableStateOf<Int?>(customResponse.code) }
    val isEnabled = rememberExpandState(customResponse.isEnabled)
    val expandState = rememberExpandState(false)

    Expandable(
        modifier = modifier.fillMaxWidth(),
        expandState = expandState.value,
        title = {
            Row(
                modifier = modifier
                    .padding(horizontal = 20.dp)
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    modifier = Modifier.weight(0.75f),
                    text = url.value,
                    color = if (isEnabled.value) Color.Black else Color.Gray,
                )
                if (path.isNotEmpty()) {
                    Icon(
                        painter = if (expandState.value) {
                            painterResource(id = R.drawable.ic_chevron_down)
                        } else {
                            painterResource(id = R.drawable.ic_chevron_up)
                        },
                        contentDescription = null,
                    )
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextInputField(
                        modifier = Modifier.weight(0.75f),
                        label = { Text(text = stringResource(id = R.string.request_field_title)) },
                        text = url.value,
                        hint = stringResource(id = R.string.request_hint),
                        maxLines = 2,
                        enabled = isEnabled.value,
                        onTextChange = { url.value = it },
                    )
                    Switch(
                        checked = isEnabled.value,
                        onCheckedChange = {
                            isEnabled.value = !isEnabled.value
                        },
                    )
                }

                TextArea(
                    enabled = isEnabled.value,
                    text = response.value,
                    label = { Text(text = stringResource(id = R.string.response_field_title)) },
                    hint = stringResource(id = R.string.response_hint),
                    onTextChange = { response.value = it },
                )

                TextInputField(
                    enabled = isEnabled.value,
                    label = {
                        Text(text = stringResource(id = R.string.response_code_field_title))
                    },
                    text = code.value?.toString() ?: "",
                    hint = stringResource(id = R.string.response_code_hint),
                    onTextChange = {
                        if (it.isNotEmpty()) {
                            code.value = it.toInt()
                        } else {
                            code.value = null
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    OutlinedButton(
                        onClick = {
                            onRemoveCustomResponse(customResponse.id)
                        },
                    ) {
                        Text(text = stringResource(id = R.string.delete))
                    }
                    OutlinedButton(
                        onClick = {
                            url.value = path
                            response.value = customResponse.response
                            code.value = customResponse.code
                            isEnabled.value = customResponse.isEnabled
                            expandState.value = !expandState.value
                        },
                    ) {
                        Text(text = stringResource(id = R.string.dismiss))
                    }
                    Button(
                        modifier = Modifier.weight(35f),
                        onClick = {
                            onSaveResponse(
                                CustomResponse(
                                    id = customResponse.id,
                                    url = url.value,
                                    response = response.value,
                                    code = code.value ?: 200,
                                    isEnabled = isEnabled.value,
                                )
                            )
                            expandState.value = !expandState.value
                        },
                    ) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
        },
        onClick = {
            expandState.value = !expandState.value
        },
    )
}