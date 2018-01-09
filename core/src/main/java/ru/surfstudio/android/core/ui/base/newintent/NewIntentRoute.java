package ru.surfstudio.android.core.ui.base.newintent;


import android.content.Intent;

import ru.surfstudio.android.core.ui.base.navigation.Route;
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityWithParamsRoute;

/**
 * разпаковывает Intent с данными, пришедший в событие onNewIntent
 * Этот интерфейс может реализовывать {@link ActivityWithParamsRoute}
 */
public interface NewIntentRoute extends Route {

    /**
     * @param newIntent
     * @return true if intent correspond to this Route
     */
    boolean parseIntent(Intent newIntent);
}
