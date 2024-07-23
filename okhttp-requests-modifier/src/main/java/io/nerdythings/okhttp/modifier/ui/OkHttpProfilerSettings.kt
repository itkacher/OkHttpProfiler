package io.nerdythings.okhttp.modifier.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.nerdythings.okhttp.modifier.data.CustomResponse
import io.nerdythings.okhttp.requests.modifier.R

@Composable
internal fun OkHttpProfilerSettings(
    allSavedPathModifications: List<Pair<String, CustomResponse>>,
    onSaveResponse: (String, String, Int) -> Unit,
    onUpdateResponse: (CustomResponse) -> Unit,
    onRemoveCustomResponse: (String) -> Unit,
    onClose: () -> Unit,
) {
    val openAddNewDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable { onClose() },
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(id = R.string.profiler_settings),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                )
            }
        },
        content = {
            ProfilerSettingsContent(
                allSavedPathModifications = allSavedPathModifications,
                onSaveResponse = onUpdateResponse,
                modifier = Modifier.padding(it),
                onRemoveCustomResponse = onRemoveCustomResponse,
            )
            if (openAddNewDialog.value) {
                AddNewRequestItemDialog(
                    onDismissRequest = {
                        openAddNewDialog.value = false
                    },
                ) { path, response, code ->
                    onSaveResponse(path, response, code)
                    openAddNewDialog.value = false
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openAddNewDialog.value = true
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_to_list),
                    contentDescription = stringResource(id = R.string.new_request_field_title),
                    tint = Color.DarkGray
                )
            }
        }
    )
}

@Composable
private fun ProfilerSettingsContent(
    allSavedPathModifications: List<Pair<String, CustomResponse>>,
    onSaveResponse: (CustomResponse) -> Unit,
    onRemoveCustomResponse: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState, true)
            .fillMaxSize(),
    ) {
        allSavedPathModifications.forEach { (path, customResponse) ->
            RequestItem(
                path = path,
                customResponse = customResponse,
                onSaveResponse = onSaveResponse,
                onRemoveCustomResponse = onRemoveCustomResponse,
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
            )
        }
        Spacer(modifier = Modifier.padding(56.dp))
    }
}

@Preview
@Composable
private fun OkHttpProfilerSettingsPreview() {
    OkHttpProfilerSettings(
        allSavedPathModifications = listOf(
            "https://www.google.com" to CustomResponse(
                id = "1",
                url = "https://www.google.com",
                response = "response",
                code = 200,
                isEnabled = true,
            ),
        ),
        onSaveResponse = { _, _, _ -> },
        onUpdateResponse = { _ -> },
        onRemoveCustomResponse = { _ -> },
        onClose = {},
    )
}
