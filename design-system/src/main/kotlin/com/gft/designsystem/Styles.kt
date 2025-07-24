package com.gft.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicReference

@Stable
interface StyleValues

@Stable
interface Style

interface DynamicStyle : Style

@Stable
fun interface StyleProducer<T : Style> {
    @Composable
    fun produce(): T
}

@Stable
fun interface StyleValuesProducer<T : Style, R : StyleValues> {
    @Composable
    fun produce(style: T): R
}

@PublishedApi
internal class StylesCache : MutableMap<Any, Any> by WeakHashMap()

@PublishedApi
internal val LocalStylesCache = staticCompositionLocalOf { StylesCache() }

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
        val cache = LocalStylesCache.current
        synchronized(cache) {
            val cachedStyleValues = cache[this]
            if (cachedStyleValues != null && cachedStyleValues is R) {
                cachedStyleValues
            } else {
                val newStyleValues = styleValuesProducer.produce(this)
                cache[this] = newStyleValues
                newStyleValues
            }
        }
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
    styleProducer: StyleProducer<T>
): T {
    val styleReference = remember(*keys) {
        AtomicReference<T?>(null)
    }
    return styleReference.get() ?: synchronized(styleReference) {
        styleReference.get() ?: styleProducer.produce().apply { styleReference.set(this) }
    }
}
