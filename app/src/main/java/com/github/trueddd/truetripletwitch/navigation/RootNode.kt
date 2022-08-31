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
import com.github.trueddd.truetripletwitch.di.ViewModelFactory
import com.github.trueddd.truetripletwitch.ui.screens.main.MainScreen
import com.github.trueddd.truetripletwitch.ui.screens.stream.StreamScreen
import org.koin.core.parameter.parametersOf

class RootNode(
    buildContext: BuildContext,
    private val viewModelFactory: ViewModelFactory,
    private val backStack: BackStack<Routing> = BackStack(
        initialElement = Routing.Main,
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<Routing>(backStack, buildContext), IntentHandler {

    override fun resolve(routing: Routing, buildContext: BuildContext) = when (routing) {
        is Routing.Main -> MainScreen(viewModelFactory.create(), backStack, buildContext)
        is Routing.Stream -> StreamScreen(viewModelFactory.create { parametersOf(routing.streamId) }, buildContext)
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
