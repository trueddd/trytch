package com.github.trueddd.truetripletwitch.navigation

import android.content.res.Configuration
import android.util.Log
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.github.trueddd.truetripletwitch.di.NodeViewModelStore
import kotlinx.coroutines.flow.*

typealias ActiveNodes = List<String>

fun RootNode.activeNodesFlow(): StateFlow<ActiveNodes> {
    return backStack.elements
        .map { elements -> elements.map { it.key.navTarget.name } }
        .distinctUntilChanged()
        .stateIn(lifecycleScope, SharingStarted.Lazily, backStack.elements.value.map { it.key.navTarget.name })
}

fun StateFlow<ActiveNodes>.disposeViewModels(viewModelStore: NodeViewModelStore): Flow<ActiveNodes> {
    var lastItem = value
    return onEach { nodes ->
        Log.d("RootNode", "Nodes change: previous($lastItem), new($nodes)")
        val left = lastItem - nodes.toSet()
        lastItem = nodes
        if (left.isEmpty()) {
            return@onEach
        }
        Log.d("RootNode", "VMs to clear: $left; Store keys: ${viewModelStore.keys}")
        left.forEach {
            viewModelStore[it]?.release()
            viewModelStore.remove(it)
        }
    }
}

fun Flow<ActiveNodes>.handleWindowRotations(window: Window): Flow<ActiveNodes> {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    return onEach { nodes ->
        if (nodes.any { it == Routing.Companion.Keys.STREAM }) {
            windowInsetsController.systemBarsBehavior = when (window.context.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
                else -> {
                    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
                }
            }
        }
    }
}
