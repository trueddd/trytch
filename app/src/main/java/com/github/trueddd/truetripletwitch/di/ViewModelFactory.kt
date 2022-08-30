package com.github.trueddd.truetripletwitch.di

import androidx.activity.ComponentActivity
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewModelFactory(
    val activity: ComponentActivity,
) {

    inline fun <reified T : StatefulViewModel<*>> create(): T {
        return activity.viewModel<T>().value
    }
}
