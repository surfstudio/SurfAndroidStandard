package ru.surfstudio.standard.f_debug.controller

/**
 * Контроллер для перехода на экран просмотра контроллеров, используемых в приложении
 */
class ShowControllersDebugControllerItem(onClickListener: () -> Unit
): BaseDebugItemController(onClickListener)

/**
 * Контроллер для перехода на экран просмотра fcm-токена
 */
class ShowFcmTokenDebugControllerItem(onClickListener: () -> Unit
): BaseDebugItemController(onClickListener)