package ru.surfstudio.android.core.ui.base.navigation.activity.route;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.navigation.Route;

/**
 * разпаковывает Intent с данными, пришедший в событие onNewIntent
 * Этот интерфейс может реализовывать {@link ActivityRoute}
 */
public interface NewIntentRoute extends Route {

    /**
     * @param newIntent
     * @return true if intent correspond to this Route
     */
    boolean parseIntent(Intent newIntent);
}
