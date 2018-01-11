package ru.surfstudio.standard.ui.base.error


import javax.inject.Inject

import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.message.MessageController

/**
 * ErrorHandler для огнорирования Http ошибок.
 */
@PerScreen
class HttpIgnoreErrorHandler @Inject
constructor(messageController: MessageController) : StandardErrorHandler(messageController) {

    override fun handleOtherError(throwable: Throwable) {}
}