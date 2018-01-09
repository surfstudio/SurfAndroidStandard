package ru.surfstudio.standard.ui.base.error;


import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.message.MessageController;
import ru.surfstudio.standard.interactor.common.network.error.ApiErrorException;

/**
 * ErrorHandler для огнорирования Http ошибок.
 * */
@PerScreen
public class HttpIgnoreErrorHandler extends StandardErrorHandler {

    @Inject
    public HttpIgnoreErrorHandler(MessageController messageController) {
        super(messageController);
    }

    @Override
    protected void handleOtherError(Throwable throwable) {
        if (!handleOrderCreateError(throwable)) {
            super.handleOtherError(throwable);
        }
    }

    private boolean handleOrderCreateError(Throwable throwable) {
        return throwable instanceof ApiErrorException;
    }
}