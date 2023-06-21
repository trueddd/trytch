package com.github.trueddd.trytch.navigation

import android.content.res.Configuration
import android.util.Log
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.github.trueddd.trytch.di.NodeViewModelStore
import kotlinx.coroutines.flow.*

typealias ActiveNodes = List<String>

fun RootNode.activeNodesFlow(): StateFlow<ActiveNodes> {
    return backStack.elements
        .map { elements -> elements.map { it.key.navTarget.name } }
        .distinctUntilChanged()
        .stateIn(lifecycleScope, SharingStarted.Lazily, backStack.elements.value.map { it.key.navTarget.name })
}

fun StateFlow<ActiveNodes>.disposeViewModels(viewModelStore: NodeViewModelStore): Flow<ActiveNodes> {
    var previousActiveNodes = value
    return onEach { activeNodes ->
        Log.d("RootNode", "Nodes change: previous($previousActiveNodes), new($activeNodes)")
        val leftNodes = previousActiveNodes - activeNodes.toSet()
        previousActiveNodes = activeNodes
        if (leftNodes.isEmpty()) {
            return@onEach
        }
        Log.d("RootNode", "VMs to clear: $leftNodes; Store keys: ${viewModelStore.keys}")
        leftNodes.forEach {
            viewModelStore[it]?.release()
            viewModelStore.remove(it)
        }
    }
}

@Suppress("DEPRECATION")
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
