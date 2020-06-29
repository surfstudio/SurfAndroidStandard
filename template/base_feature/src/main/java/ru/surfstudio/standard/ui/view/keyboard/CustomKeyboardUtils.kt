package ru.surfstudio.standard.ui.view.keyboard

import ru.surfstudio.standard.ui.view.keyboard.keys.EmptyKey
import ru.surfstudio.standard.ui.view.keyboard.keys.Key
import ru.surfstudio.standard.ui.view.keyboard.keys.TextKey

object CustomKeyboardUtils {

    private val oneToNine = listOf(
            TextKey("1"),
            TextKey("2"),
            TextKey("3"),
            TextKey("4"),
            TextKey("5"),
            TextKey("6"),
            TextKey("7"),
            TextKey("8"),
            TextKey("9")
    )

    private val zeroTextKey get() = TextKey("0")

    fun createKeyBoard(leftButton: Key? = null, rightButton: Key? = null) =
            mutableListOf<Key>().apply {
                addAll(oneToNine)
                add(leftButton ?: EmptyKey())
                add(zeroTextKey)
                add(rightButton ?: EmptyKey())
            }
}