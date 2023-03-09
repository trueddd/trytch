package com.github.trueddd.truetripletwitch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel

abstract class StatelessViewModel : ViewModel() {

    open fun release() {
        viewModelScope.cancel()
    }
}
