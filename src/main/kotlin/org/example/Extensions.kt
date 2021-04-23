package org.example

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

suspend fun <T, V> Iterable<T>.asyncAll(coroutine: suspend (T) -> V): Iterable<V> = coroutineScope {
    this@asyncAll.map { async { coroutine(it) } }.awaitAll()
}

suspend fun <T, V> Iterable<T>.asyncAll(batchSize: Int, coroutine: suspend (T) -> V): Iterable<V> = coroutineScope {
    this@asyncAll.chunked(batchSize).map { e -> e.asyncAll(coroutine) }.flatten()
}
