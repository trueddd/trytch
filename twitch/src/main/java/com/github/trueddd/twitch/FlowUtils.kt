package com.github.trueddd.twitch

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun <T> Flow<T>.onEachWithLock(lock: Mutex = Mutex(), action: suspend (T) -> Unit): Flow<T> {
    return onEach {
        lock.withLock {
            action(it)
        }
    }
}
