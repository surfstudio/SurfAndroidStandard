package ru.surfstudio.android.core.ui.base.screen.view;

import android.support.annotation.IdRes;

/**
 * интерфейс для вью, поддерживающей навигацию фрагментов
 */
public interface FragmentContainer {
    /**
     * @return вью контейнер id или 0
     */
    @IdRes
    int getContentContainerViewId();
}
