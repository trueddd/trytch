package com.github.trueddd.truetripletwitch

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.LocalIntegrationPoint
import com.github.trueddd.truetripletwitch.navigation.RootNode
import com.github.trueddd.truetripletwitch.navigation.activeNodesFlow
import com.github.trueddd.truetripletwitch.navigation.disposeViewModels
import com.github.trueddd.truetripletwitch.navigation.handleWindowRotations
import com.github.trueddd.truetripletwitch.ui.NodeActivity
import com.github.trueddd.truetripletwitch.ui.screens.splash.SplashViewModel
import com.github.trueddd.truetripletwitch.ui.theme.TrueTripleTwitchTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class MainActivity : NodeActivity() {

    private val splashViewModel by inject<SplashViewModel>()

    private var rootNode: RootNode? = null
        set(value) {
            field = value
            value?.let { setupNavigationListener(it) }
        }

    private fun setupNavigationListener(rootNode: RootNode) {
        rootNode.activeNodesFlow()
            .disposeViewModels(viewModelStore = get())
            .handleWindowRotations(window)
            .launchIn(lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!splashViewModel.initialized) {
            lifecycleScope.launch {
                splashViewModel.initialize()
            }
            installSplashScreen().apply {
                setKeepOnScreenCondition { !splashViewModel.initialized }
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            TrueTripleTwitchTheme {
                CompositionLocalProvider(
                    LocalIntegrationPoint provides appyxIntegrationPoint,
                    LocalImageLoader provides createImageLoader(),
                ) {
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
