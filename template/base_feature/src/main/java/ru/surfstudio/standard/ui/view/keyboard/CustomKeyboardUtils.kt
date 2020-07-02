package ru.surfstudio.standard.ui.view.keyboard

/**
 * Утилиты для создания кастомной клавиатуры [KeyboardView]
 * todo удалить, если не требуется на проекте
 */
object CustomKeyboardUtils {

    val oneToNine = listOf(
            TextKey("1"),
            TextKey("2", "a b c"),
            TextKey("3", "d e f"),
            TextKey("4", "g h i"),
            TextKey("5", "j k l"),
            TextKey("6", "m n o"),
            TextKey("7", "p q r s"),
            TextKey("8", "t u v"),
            TextKey("9", "w x y z")
    )

    val zeroTextKey get() = TextKey("0")

    fun createKeyBoard(leftButton: Key? = null, rightButton: Key? = null) =
            mutableListOf<Key>().apply {
                addAll(oneToNine)
                add(leftButton ?: EmptyKey())
                add(zeroTextKey)
                add(rightButton ?: EmptyKey())
            }
}