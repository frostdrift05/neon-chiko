package com.Meztido0.Dewgov.presentation.app

import androidx.lifecycle.ViewModel
import com.Meztido0.Dewgov.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _backStack = MutableStateFlow<List<Screen>>(emptyList())
    val backStack: StateFlow<List<Screen>> = _backStack.asStateFlow()

    fun finishSplash() {
        _backStack.value = emptyList()
        _screen.value = Screen.Menu
    }

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current != Screen.Splash && current != target) {
            _backStack.value = _backStack.value + current
        }
        _screen.value = target
    }

    fun navigateFromRun(target: Screen) {
        _backStack.value = listOf(Screen.Menu)
        _screen.value = target
    }

    fun back() {
        val stack = _backStack.value
        if (stack.isEmpty()) {
            return
        }
        _screen.value = stack.last()
        _backStack.value = stack.dropLast(1)
    }

    fun goMenu() {
        _backStack.value = emptyList()
        _screen.value = Screen.Menu
    }
}
