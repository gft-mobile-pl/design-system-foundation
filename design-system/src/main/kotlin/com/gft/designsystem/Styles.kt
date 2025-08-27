package com.gft.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.gft.concurrency.Lock
import com.gft.concurrency.withLock

interface StyleValues

interface Style

interface DynamicStyle : Style

fun interface StyleProducer<T : Style> {
    @Composable
    fun produce(): T
}

fun interface StyleValuesProducer<T : Style, R : StyleValues> {
    @Composable
    fun produce(style: T): R
}

private class StyleContainer<T>(
    var value: T,
)

@PublishedApi
internal expect class StyleValuesCache() {
    @Composable
    inline fun <T : Style, reified R : StyleValues> getOrCreate(
        style: T,
        styleValuesProducer: StyleValuesProducer<T, R>,
    ): R
}

@PublishedApi
internal val LocalStyleValuesCache = staticCompositionLocalOf { StyleValuesCache() }

@Composable
inline fun <T : Style, reified R : StyleValues> T.produceStyleValues(
    styleValuesProducer: StyleValuesProducer<T, R>,
): R {
    return if (this is DynamicStyle) {
        val localStyleValuesCache = remember(this) {
            mutableStateOf<R?>(null)
        }
        UpdateLocalStyleCache(localStyleValuesCache) { styleValuesProducer.produce(this) }
        localStyleValuesCache.value!!
    } else {
        LocalStyleValuesCache.current.getOrCreate(this, styleValuesProducer)
    }
}

@PublishedApi
@Composable
internal fun <T : StyleValues> UpdateLocalStyleCache(
    localStyleValuesCache: MutableState<T?>,
    styleValuesProducer: @Composable () -> T,
) {
    localStyleValuesCache.value = styleValuesProducer()
}

@Composable
fun <T : Style> rememberStyle(
    vararg keys: Any?,
    styleProducer: StyleProducer<T>,
): T {
    val (styleReference, lock) = remember(*keys) {
        StyleContainer<T?>(null) to Lock()
    }

    return styleReference.value ?: lock.withLock {
        styleReference.value ?: styleProducer.produce().apply { styleReference.value = this }
    }
}
