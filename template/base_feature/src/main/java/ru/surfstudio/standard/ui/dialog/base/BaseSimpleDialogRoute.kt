package ru.surfstudio.standard.ui.dialog.base

import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

/**
 * Базовый класс роута простого диалога с результатом типа [SimpleResult].
 */
abstract class BaseSimpleDialogRoute : DialogRoute(), ResultRoute<SimpleResult> {

    /**
     * Идентификатор диалога, используется чтобы не возникало коллизий
     * при получении результатов диалогов запущенных на разных экранах
     */
    protected abstract val dialogId: String

    override fun getId(): String {
        return super.getId() + dialogId
    }
}
