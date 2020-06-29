package ru.surfstudio.standard.ui.view.keyboard.controller

import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.standard.ui.view.keyboard.keys.Key

/**
 * Базовый ItemController для создания кнопки для кастомной клавиатуры.
 */
abstract class BaseKeyController<T: Key, H: BaseKeyHolder<T>> : BindableItemController<T, H>()