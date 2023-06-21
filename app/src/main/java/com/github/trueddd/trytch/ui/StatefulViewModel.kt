package com.github.trueddd.trytch.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class StatefulViewModel<S> : StatelessViewModel() {

    protected abstract fun initialState(): S

    private val _stateFlow by lazy { MutableStateFlow(initialState()) }
    val stateFlow: StateFlow<S>
        get() = _stateFlow

    fun updateState(modifyBlock: (S) -> S) {
        _stateFlow.update(modifyBlock)
    }
}
