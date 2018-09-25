package ru.surfstudio.standard.f_debug.debug.controllers

/**
 * Контроллер для перехода на экран просмотра контроллеров, используемых в приложении
 */
class ShowControllersDebugItemController(onClickListener: () -> Unit
): BaseDebugItemController(onClickListener)

/**
 * Контроллер для перехода на экран просмотра fcm-токена
 */
class ShowFcmTokenDebugItemController(onClickListener: () -> Unit
): BaseDebugItemController(onClickListener)