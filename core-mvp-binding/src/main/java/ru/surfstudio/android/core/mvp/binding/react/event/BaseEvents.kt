package ru.surfstudio.android.core.mvp.binding.react.event

/**
 * Классы базовых эвентов.
 *
 * Метод [toString] переопределен для удобства логгирования
 */

open class OpenScreen(val name: String) : Event {
    override fun toString() = "Open screen: $name"
}

open class SwipeRefresh : Event {
    override fun toString() = "SwipeRefresh"
}

open class ReloadData : Event {
    override fun toString() = "ReloadData"
}

open class LoadNextData : Event {
    override fun toString() = "LoadNextData"
}