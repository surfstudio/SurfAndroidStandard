package ru.surfstudio.android.core.ui.navigation.activity.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.surfstudio.android.core.app.ActiveActivityHolder;
import ru.surfstudio.android.core.ui.navigation.Navigator;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute;

/**
 * глобальный навигатор для перехода по экранам не имея доступ
 * к контексту активити (из слоя Interactor)
 */
public class GlobalNavigator implements Navigator {
    private final Context context;
    private final ActiveActivityHolder activityHolder;

    public GlobalNavigator(Context context, ActiveActivityHolder activityHolder) {
        this.context = context;
        this.activityHolder = activityHolder;
    }

    /**
     * Запуск активити.
     *
     * @param route роутер
     * @return {@code true} если активити успешно запущен, иначе {@code false}
     */
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
}
