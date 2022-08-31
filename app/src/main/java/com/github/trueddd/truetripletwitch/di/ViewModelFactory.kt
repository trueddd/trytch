package com.github.trueddd.truetripletwitch.di

import androidx.activity.ComponentActivity
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersDefinition

class ViewModelFactory(
    val activity: ComponentActivity,
) {

    inline fun <reified T : StatefulViewModel<*>> create(
        noinline parameters: ParametersDefinition? = null
    ): T {
        return activity.viewModel<T>(parameters = parameters).value
    }
}
