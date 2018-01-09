package ru.surfstudio.android.core.ui.base.screen.fragment;


import javax.inject.Inject;

import ru.surfstudio.android.core.ui.base.error.ErrorHandler;
import ru.surfstudio.android.core.ui.base.screen.view.HandleableErrorView;

/**
 * базовый класс для FragmentView, поддрерживающий обработку ошибок из презентера
 * предоставляет стандартную обработку ошибок, для изменения логики обработки можно переопределить
 * {@link #handleError(Throwable)} или {@link #getErrorHandler()}
 */
public abstract class BaseHandleableErrorFragmentView extends CoreFragmentView
        implements HandleableErrorView {

    @Inject
    ErrorHandler standardErrorHandler;

    @Override
    public void handleError(Throwable error){
        getErrorHandler().handleError(error);
    }

    protected ErrorHandler getErrorHandler() {
        return standardErrorHandler;
    }
}
