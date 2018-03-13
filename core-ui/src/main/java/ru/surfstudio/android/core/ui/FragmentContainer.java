package ru.surfstudio.android.core.ui;

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
