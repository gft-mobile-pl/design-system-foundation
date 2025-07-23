package com.gft.designsystem

import androidx.compose.runtime.Composable
import com.gft.concurrency.Lock
import com.gft.concurrency.withLock
import java.util.WeakHashMap

@PublishedApi
internal actual class StyleValuesCache {

    val lock = Lock()
    val map = WeakHashMap<Style, StyleValues>()

    @Composable
    actual inline fun <T : Style, reified R : StyleValues> getOrCreate(
        style: T,
        styleValuesProducer: StyleValuesProducer<T, R>,
    ): R = lock.withLock {
        val cachedStyleValues = map[style]
        if (cachedStyleValues != null && cachedStyleValues is R) {
            cachedStyleValues
        } else {
            val newStyleValues = styleValuesProducer.produce(style)
            map[style] = newStyleValues
            newStyleValues
        }
    }
}
