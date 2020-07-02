package ru.surfstudio.standard.ui.view.keyboard.controller

import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.standard.ui.view.keyboard.Key

/**
 * Базовый ItemController для создания кнопки для кастомной клавиатуры.
 * todo удалить, если не требуется на проекте
 */
abstract class BaseKeyController<T : Key, H : BaseKeyHolder<T>> : BindableItemController<T, H>()