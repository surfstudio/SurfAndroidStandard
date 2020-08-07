package ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple

import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

/**
 * Базовый класс роута простого диалога с результатом типа [SimpleDialogResult].
 */
abstract class BaseSimpleDialogRoute : DialogRoute(), ResultRoute<SimpleDialogResult>
