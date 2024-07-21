package com.localebro.requestsModifier.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.itkacher.requestsModifier.R

@Composable
internal fun AddNewRequestItemDialog(
    onDismissRequest: () -> Unit,
    onSaveResponse: (String, String, Int) -> Unit,
) {
    val url = remember { mutableStateOf("") }
    val response = remember { mutableStateOf("") }
    val code = remember { mutableIntStateOf(200) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.new_request_field_title),
                    fontSize = 16.sp,
                )
                TextInputField(
                    label = { Text(text = stringResource(id = R.string.request_field_title)) },
                    text = url.value,
                    hint = stringResource(id = R.string.request_hint),
                    maxLines = 2,
                    onTextChange = { url.value = it },
                )
                TextArea(
                    text = response.value,
                    label = { Text(text = stringResource(id = R.string.response_field_title)) },
                    hint = stringResource(id = R.string.response_hint),
                    onTextChange = { response.value = it },
                    maxLines = 5,
                )
                TextInputField(
                    label = {
                        Text(text = stringResource(id = R.string.response_code_field_title))
                    },
                    text = code.intValue.toString(),
                    hint = stringResource(id = R.string.response_code_hint),
                    onTextChange = {
                        if (it.isNotEmpty()) {
                            code.intValue = it.toInt()
                        } else {
                            code.intValue = 200
                        }
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onSaveResponse(
                            url.value,
                            response.value,
                            code.intValue
                        )
                    },
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    }
}