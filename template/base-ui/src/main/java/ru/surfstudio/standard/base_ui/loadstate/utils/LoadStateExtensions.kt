package ru.surfstudio.standard.base_ui.loadstate.utils

import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer

fun PlaceHolderViewContainer.clickAndFocus(value: Boolean) {
    isClickable = value
    isFocusable = value
    isFocusableInTouchMode = value
}