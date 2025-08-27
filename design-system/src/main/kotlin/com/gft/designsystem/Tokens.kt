package com.gft.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import kotlin.reflect.KProperty

sealed interface Token<T> {

    @Composable
    fun resolve(): T
}

interface ReferencingToken<T> : Token<T> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): ReferencingToken<T> = this
}

internal class ValueToken<T>(
    private val value: T,
) : Token<T> {

    @Composable
    @ReadOnlyComposable
    override fun resolve(): T = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ValueToken<*>) return false

        return value == other.value
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }
}

internal class ReferenceToValue<T>(
    private val valueResolver: @Composable () -> T,
) : ReferencingToken<T> {

    @Composable
    override fun resolve(): T = valueResolver.invoke()
}

fun <T> Token(value: T): Token<T> = ValueToken(value)

fun <T> Token(valueResolver: @Composable () -> T): Token<T> = ReferenceToValue(valueResolver)

fun <T> Style.valueReference(valueProvider: @Composable () -> T): ReferencingToken<T> = ReferenceToValue(valueProvider)

fun <T> TokenReference(tokenProvider: @Composable () -> Token<T>): ReferencingToken<T> = ReferenceToValue { tokenProvider().resolve() }

fun <T> Style.tokenReference(tokenProvider: @Composable () -> Token<T>): ReferencingToken<T> = ReferenceToValue { tokenProvider().resolve() }
