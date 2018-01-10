package ru.surfstudio.standard.ui.base.error;


import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.message.MessageController;

/**
 * ErrorHandler для огнорирования Http ошибок.
 */
@PerScreen
public class HttpIgnoreErrorHandler extends StandardErrorHandler {

    @Inject
    public HttpIgnoreErrorHandler(MessageController messageController) {
        super(messageController);
    }

    @Override
    protected void handleOtherError(Throwable throwable) {
    }
}