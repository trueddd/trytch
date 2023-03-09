package com.github.trueddd.truetripletwitch.navigation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.children.nodeOrNull
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.github.trueddd.truetripletwitch.ui.screens.main.MainScreen
import com.github.trueddd.truetripletwitch.ui.screens.splash.SplashScreen
import com.github.trueddd.truetripletwitch.ui.screens.stream.StreamScreen
import com.github.trueddd.truetripletwitch.ui.screens.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

class RootNode(
    buildContext: BuildContext,
    val backStack: BackStack<Routing> = BackStack(
        initialElement = Routing.Splash,
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<Routing>(backStack, buildContext), IntentHandler, KoinComponent {

    override fun resolve(routing: Routing, buildContext: BuildContext) = when (routing) {
        is Routing.Splash -> SplashScreen(viewModel(Routing.Companion.Keys.SPLASH), backStack, buildContext)
        is Routing.Main -> MainScreen(viewModel(Routing.Companion.Keys.MAIN), backStack, buildContext)
        is Routing.Stream -> StreamScreen(viewModel(Routing.Companion.Keys.STREAM) { parametersOf(routing.channel) }, buildContext)
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(navModel = backStack, modifier = modifier.fillMaxSize()) {
            children<Routing> { child ->
                child()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        children.value.values
            .mapNotNull { it.nodeOrNull as? IntentHandler }
            .lastOrNull()
            ?.onNewIntent(intent)
    }
}
