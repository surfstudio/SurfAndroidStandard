package ru.surfstudio.standard.ui.view.keyboard

import ru.surfstudio.standard.ui.view.keyboard.keys.EmptyKey
import ru.surfstudio.standard.ui.view.keyboard.keys.Key
import ru.surfstudio.standard.ui.view.keyboard.keys.TextKey

object KeyBoardConsts {

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

    val zeroTextKey get() = TextKey("0")

    val emptyKey get() = EmptyKey()

    val simpleKeyBoardItems: List<Key> = mutableListOf<Key>()
            .apply {
                addAll(oneToNine)
                add(emptyKey)
                add(zeroTextKey)
                add(emptyKey)
            }

    fun createKeyBoarWithRightBtn(rightKey: Key): List<Key> =
            mutableListOf<Key>()
                    .apply {
                        addAll(oneToNine)
                        add(emptyKey)
                        add(zeroTextKey)
                        add(rightKey)
                    }

    fun createKeyBoarWithLeftBtn(leftKey: Key): List<Key> =
            mutableListOf<Key>()
                    .apply {
                        addAll(oneToNine)
                        add(leftKey)
                        add(zeroTextKey)
                        add(emptyKey)
                    }

    fun createKeyBoarWithLeftAndRightBtn(leftKey: Key, rightKey: Key): List<Key> =
            mutableListOf<Key>()
                    .apply {
                        addAll(oneToNine)
                        add(leftKey)
                        add(zeroTextKey)
                        add(rightKey)
                    }
}