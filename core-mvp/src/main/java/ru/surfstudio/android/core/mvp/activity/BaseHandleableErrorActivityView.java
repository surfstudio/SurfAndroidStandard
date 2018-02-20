package ru.surfstudio.android.core.mvp.activity;


import javax.inject.Inject;

import ru.surfstudio.android.core.mvp.error.ErrorHandler;
import ru.surfstudio.android.core.mvp.view.HandleableErrorView;

/**
 * базовый коасс для ActivityView, обработку ошибок из презентера
 * и предоставляет стандартную обработку ошибок, для изменения логики обработки можно переопределить
 * {@link #handleError(Throwable)} или {@link #getErrorHandler()}
 */
public abstract class BaseHandleableErrorActivityView extends CoreActivityView
        implements HandleableErrorView {

    @Inject
    ErrorHandler standardErrorHandler;

    @Override
    public void handleError(Throwable error) {
        getErrorHandler().handleError(error);
    }

    protected ErrorHandler getErrorHandler() {
        return standardErrorHandler;
    }
}
