package ru.surfstudio.android.core.ui.base.navigation.activity.route;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.surfstudio.android.core.ui.base.navigation.Route;

/**
 * маршрут, по которому следует открыть активити
 * см {@link Route}
 */
@SuppressWarnings("squid:S1610")
public abstract class ActivityRoute implements Route {

    public abstract Intent prepareIntent(Context context);

    public Bundle prepareBundle() {
        return null;
    }

}