package com.milesaway.android.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val SIDE_EFFECTS_KEY = "side-effects_key"

abstract class BaseViewModel<Event : UiEvent, State : UiState, Effect : UiEffect>  : ViewModel() {

    private val initialState: State by lazy {
        initState()
    }

    val currentState: State
        get() = uiState.value

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _effects: Channel<Effect> = Channel()
    val effects = _effects.receiveAsFlow()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            events.collect {
                handleEvent(it)
            }
        }
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }

    abstract fun handleEvent(event: Event)

    abstract fun initState() : State
}