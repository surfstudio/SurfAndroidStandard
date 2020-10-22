package ru.surfstudio.standard.ui.view.keyboard

/**
 * Утилиты для создания кастомной клавиатуры [KeyboardView]
 * todo удалить, если не требуется на проекте
 */
object CustomKeyboardUtils {

    /**
     * Формирует список кнопок клавиатуры в правильном порядке
     *
     * @param onClickListener обработчик нажатия для кнопок с цифрами
     * @param leftButton кнопка слева от кнопки ноль
     * @param rightButton кнопка справа от кнопки ноль
     */
    fun getKeysList(
            onClickListener: (String) -> Unit,
            leftButton: Key,
            rightButton: Key
    ): List<Key> = mutableListOf<Key>().apply {
        addAll(getOneToNineKeys(onClickListener))
        add(leftButton)
        add(getZeroKey(onClickListener))
        add(rightButton)
    }

    private fun getZeroKey(onClickListener: (String) -> Unit) =
            TextKey("0") { onClickListener(it) }

    private fun getOneToNineKeys(onClickListener: (String) -> Unit): List<TextKey> = listOf(
            TextKey("1") { onClickListener(it) },
            TextKey("2", "a b c") { onClickListener(it) },
            TextKey("3", "d e f") { onClickListener(it) },
            TextKey("4", "g h i") { onClickListener(it) },
            TextKey("5", "j k l") { onClickListener(it) },
            TextKey("6", "m n o") { onClickListener(it) },
            TextKey("7", "p q r s") { onClickListener(it) },
            TextKey("8", "t u v") { onClickListener(it) },
            TextKey("9", "w x y z") { onClickListener(it) }
    )
}