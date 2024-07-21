package com.localebro.requestsModifier.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize

private val animSpec = tween<IntSize>(300)

private val LocalExpandState = compositionLocalOf<Boolean?> { null }

@Composable
fun rememberExpandState(initial: Boolean = false): MutableState<Boolean> {
    val external = LocalExpandState.current
    return rememberSaveable(external) { mutableStateOf(external ?: initial) }
}

@Composable
fun Expandable(
    expandState: Boolean,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (expandState: Boolean) -> Unit,
) {
    Column(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
        ) {
            onClick(expandState)
        },
    ) {
        title()
        AnimatedVisibility(
            modifier = modifier,
            visible = expandState,
            enter = expandVertically(animationSpec = animSpec),
            exit = shrinkVertically(animationSpec = animSpec),
        ) {
            content()
        }
    }
}
