package com.gft.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.gft.concurrency.Lock
import com.gft.concurrency.withLock

@Stable
interface Module {

    @ModuleInitialization
    @Composable
    fun initialize() = Unit
}

@Immutable
interface Colors : Module

@Immutable
interface Typography : Module

@Stable
interface Styles : Module

@Immutable
open class DesignSystemModules<out ColorSchemeType : Colors, out TypographyType : Typography, out StylesType : Styles>(
    val colors: ColorSchemeType,
    val typography: TypographyType,
    val styles: StylesType,
) {
    @Composable
    open fun initialize() {
        initialize(colors)
        initialize(typography)
        initialize(styles)
    }

    @OptIn(ModuleInitialization::class)
    @Composable
    protected fun initialize(module: Module) {
        val (moduleStatus, lock) = remember(module) {
            ModuleInitializationStatus(initialized = false) to Lock()
        }

        if (!moduleStatus.initialized) lock.withLock {
            if (!moduleStatus.initialized) {
                module.initialize()
                moduleStatus.initialized = true
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DesignSystemModules<*, *, *>

        if (colors != other.colors) return false
        if (typography != other.typography) return false
        if (styles != other.styles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = colors.hashCode()
        result = 31 * result + typography.hashCode()
        result = 31 * result + styles.hashCode()
        return result
    }

    private class ModuleInitializationStatus(
        var initialized: Boolean
    )
}

@Stable
open class DesignSystemModulesProvider<ColorSchemeType : Colors, TypographyType : Typography, StylesType : Styles>(
    private val localComposition: ProvidableCompositionLocal<out DesignSystemModules<ColorSchemeType, TypographyType, StylesType>>,
) {
    val colors: ColorSchemeType
        @Composable @ReadOnlyComposable get() = localComposition.current.colors
    val typography: TypographyType
        @Composable @ReadOnlyComposable get() = localComposition.current.typography
    val styles: StylesType
        @Composable @ReadOnlyComposable get() = localComposition.current.styles
}


@RequiresOptIn(
    message = "This method should only be called within the 'DesignSystemModules.initialize()' method or in methods overriding it. When overriding 'Module.initialize()', remember to decide whether the super implementation should be invoked.",
    level = RequiresOptIn.Level.WARNING
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
annotation class ModuleInitialization
