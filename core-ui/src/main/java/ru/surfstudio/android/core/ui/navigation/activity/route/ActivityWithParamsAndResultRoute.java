package ru.surfstudio.android.core.ui.navigation.activity.route;


import android.content.Intent;

import java.io.Serializable;

/**
 * см {@link ActivityRoute}
 *
 * @param <T> тип результата
 */
public abstract class ActivityWithParamsAndResultRoute<T extends Serializable> extends ActivityWithResultRoute<T> {

    public ActivityWithParamsAndResultRoute() {
    }

    public ActivityWithParamsAndResultRoute(Intent intent) {
    }

}
