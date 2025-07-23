package com.gft.designsystem

import androidx.compose.runtime.Composable
import com.gft.concurrency.Lock
import com.gft.concurrency.withLock
import platform.Foundation.NSMapTable

@PublishedApi
internal actual class StyleValuesCache {

    val lock = Lock()
    val map = NSMapTable.weakToStrongObjectsMapTable()

    @Composable
    actual inline fun <T : Style, reified R : StyleValues> getOrCreate(
        style: T,
        styleValuesProducer: StyleValuesProducer<T, R>,
    ): R = lock.withLock {
        val cachedStyleValues = map.objectForKey(style)
        if (cachedStyleValues != null && cachedStyleValues is R) {
            cachedStyleValues
        } else {
            val newStyleValues = styleValuesProducer.produce(style)
            map.setObject(style, newStyleValues)
            newStyleValues
        }
    }
}
