package com.github.trueddd.trytch.navigation

import android.content.Intent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.children.nodeOrNull
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.transitionhandler.rememberBackstackSlider
import com.github.trueddd.trytch.ui.screens.main.MainScreen
import com.github.trueddd.trytch.ui.screens.profile.ProfileScreen
import com.github.trueddd.trytch.ui.screens.stream.StreamScreen
import com.github.trueddd.trytch.ui.screens.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf

class RootNode(
    buildContext: BuildContext,
    val backStack: BackStack<Routing> = BackStack(
        initialElement = Routing.Main,
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<Routing>(backStack, buildContext), IntentHandler, KoinComponent {

    override fun resolve(navTarget: Routing, buildContext: BuildContext) = when (navTarget) {
        is Routing.Main -> MainScreen(
            mainViewModel = viewModel(Routing.Keys.Main),
            backStack = backStack,
            buildContext = buildContext
        )
        is Routing.Stream -> StreamScreen(
            streamViewModel = viewModel(Routing.Keys.Stream) { parametersOf(navTarget.channel) },
            emotesPanelViewModel = viewModel(Routing.Keys.EmotesPanel),
            buildContext = buildContext,
        )
        is Routing.Profile -> ProfileScreen(
            profileViewModel = viewModel(Routing.Keys.Profile) { parametersOf(backStack) },
            buildContext = buildContext
        )
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            navModel = backStack,
            modifier = modifier.fillMaxSize(),
            transitionHandler = rememberBackstackSlider(
                transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }
            ),
        ) {
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
