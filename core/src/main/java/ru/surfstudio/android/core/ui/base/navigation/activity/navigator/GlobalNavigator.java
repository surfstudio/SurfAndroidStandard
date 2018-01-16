package ru.surfstudio.android.core.ui.base.navigation.activity.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.ui.base.navigation.Navigator;
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityRoute;
import ru.surfstudio.android.core.util.ActiveActivityHolder;

/**
 * глобальный навигатор для перехода по экранам не имея доступ
 * к контексту активити (из слоя Interactor)
 */
@PerApplication
public class GlobalNavigator implements Navigator {
    private final Context context;
    private final ActiveActivityHolder activityHolder;

    @Inject
    GlobalNavigator(Context context, ActiveActivityHolder activityHolder) {
        this.context = context;
        this.activityHolder = activityHolder;
    }

    public boolean start(ActivityRoute route) {
        Activity activity = activityHolder.getActivity();
        Context localContext = activity != null ? activity : context;
        Intent intent = route.prepareIntent(localContext);
        Bundle bundle = route.prepareBundle();
        if (intent.resolveActivity(localContext.getPackageManager()) != null) {
            localContext.startActivity(intent, bundle);
            return true;
        }

        return false;
    }

    public boolean finishCurrent() {
        Activity activity = activityHolder.getActivity();
        if (activity != null && activity.isFinishing()) {
            activity.finish();
            return true;
        }

        return false;
    }

    public boolean finishAffinity() {
        Activity activity = activityHolder.getActivity();
        if (activity != null) {
            ActivityCompat.finishAffinity(activity);
            return true;
        }

        return false;
    }
}
