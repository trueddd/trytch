package com.github.trueddd.truetripletwitch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.LocalIntegrationPoint
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.github.trueddd.truetripletwitch.di.NodeViewModelStore
import com.github.trueddd.truetripletwitch.navigation.RootNode
import com.github.trueddd.truetripletwitch.ui.theme.TrueTripleTwitchTheme
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.get

class MainActivity : NodeActivity() {

    private var rootNode: RootNode? = null
        set(value) {
            field = value
            value?.let { setupNavigationListener(it) }
        }

    private fun setupNavigationListener(rootNode: RootNode) {
        val viewModelStore = get<NodeViewModelStore>()
        var lastItem = rootNode.backStack.elements.value.map { it.key.routing.name }
        rootNode.backStack.elements
            .map { elements -> elements.map { it.key.routing.name } }
            .distinctUntilChanged()
            .onEach { nodes ->
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
            .launchIn(lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrueTripleTwitchTheme(dynamicColor = false) {
                CompositionLocalProvider(LocalIntegrationPoint provides integrationPoint) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NodeHost(integrationPoint = LocalIntegrationPoint.current) { context ->
                            RootNode(context).also { rootNode = it }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rootNode = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { rootNode?.onNewIntent(it) }
    }
}
