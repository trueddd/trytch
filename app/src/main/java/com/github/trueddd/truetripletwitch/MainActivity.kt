package com.github.trueddd.truetripletwitch

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.integration.NodeFactory
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.LocalIntegrationPoint
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.bumble.appyx.core.modality.BuildContext
import com.github.trueddd.truetripletwitch.di.ViewModelFactory
import com.github.trueddd.truetripletwitch.ui.navigation.RootNode
import com.github.trueddd.truetripletwitch.ui.theme.TrueTripleTwitchTheme

class MainActivity : NodeActivity() {

    private val rootNode = RootNode(
        buildContext = BuildContext.root(null),
        viewModelFactory = ViewModelFactory(this)
    )

    private val nodeFactory = NodeFactory { rootNode }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalIntegrationPoint provides integrationPoint
            ) {
                TrueTripleTwitchTheme(dynamicColor = false) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NodeHost(
                            integrationPoint = LocalIntegrationPoint.current,
                            factory = nodeFactory,
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { rootNode.onNewIntent(it) }
    }
}
