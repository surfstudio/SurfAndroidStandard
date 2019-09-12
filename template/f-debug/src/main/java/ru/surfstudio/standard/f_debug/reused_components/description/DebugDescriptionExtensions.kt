package ru.surfstudio.standard.f_debug.reused_components.description

import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.standard.f_debug.reused_components.controllers.CustomControllerDescriptionItemController

private val descriptionItemController = CustomControllerDescriptionItemController()

fun ItemList.addDescription(description: String): ItemList {
    add(description, descriptionItemController)
    return this
}