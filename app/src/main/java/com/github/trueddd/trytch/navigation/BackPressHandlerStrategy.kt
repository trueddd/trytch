package com.github.trueddd.trytch.navigation

import com.bumble.appyx.core.navigation.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.BackStackElements
import com.bumble.appyx.navmodel.backstack.operation.Pop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BackPressHandlerStrategy<NavTarget : Any> : BaseBackPressHandlerStrategy<NavTarget, BackStack.State>() {

    private val interceptors = mutableListOf<() -> Boolean>()

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        navModel.elements.map(::areThereStashedElements)
    }

    private fun areThereStashedElements(elements: BackStackElements<NavTarget>) =
        elements.any { it.targetState == BackStack.State.STASHED }

    override fun onBackPressed() {
        if (interceptors.isEmpty() || interceptors.all { it() }) {
            navModel.accept(Pop())
        }
    }

    fun addInterceptor(interceptor: () -> Boolean) {
        interceptors.add(interceptor)
    }

    fun removeInterceptor(interceptor: () -> Boolean) {
        interceptors.remove(interceptor)
    }
}
