package com.gft.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun DesignSystem(
    modules: DesignSystemModules<*, *, *>,
    content: @Composable () -> Unit,
) {
    modules.initialize()
    CompositionLocalProvider(
        LocalStyleValuesCache provides StyleValuesCache(),
        content = content
    )
}
