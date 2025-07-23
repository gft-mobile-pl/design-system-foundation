package com.gft.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonSkippableComposable

@Composable
@NonSkippableComposable
fun DesignSystem(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalStyleValuesCache provides StyleValuesCache(),
        content = content
    )
}
