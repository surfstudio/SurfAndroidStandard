package ru.surfstudio.standard.f_debug.injector

import ru.surfstudio.standard.f_debug.DebugInteractor

class DebugAppComponent {
    private val debugInteractor = DebugInteractor()
    fun debugInteractor() = debugInteractor
}