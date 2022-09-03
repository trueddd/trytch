package com.github.trueddd.truetripletwitch.ui.screens

import android.util.Log
import com.github.trueddd.truetripletwitch.di.NodeViewModelStore
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.ParametersDefinition

inline fun <reified T : StatefulViewModel<*>> KoinComponent.viewModel(key: String, noinline parametersDefinition: ParametersDefinition? = null): T {
    val store = get<NodeViewModelStore>()
    return (store[key] as? T)?.also {
        Log.d("ViewModelStore", "Found instance in store: returning")
    } ?: get<T>(parameters = parametersDefinition).also {
        Log.d("ViewModelStore", "Created new instance")
        store[key] = it
    }
}
