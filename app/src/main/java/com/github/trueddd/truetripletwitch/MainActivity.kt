package com.github.trueddd.truetripletwitch

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.LocalIntegrationPoint
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.github.trueddd.truetripletwitch.di.ViewModelFactory
import com.github.trueddd.truetripletwitch.navigation.RootNode
import com.github.trueddd.truetripletwitch.ui.theme.TrueTripleTwitchTheme

class MainActivity : NodeActivity() {

    private var rootNode: RootNode? = null

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
                            RootNode(context, ViewModelFactory(this))
                                .also { rootNode = it }
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
