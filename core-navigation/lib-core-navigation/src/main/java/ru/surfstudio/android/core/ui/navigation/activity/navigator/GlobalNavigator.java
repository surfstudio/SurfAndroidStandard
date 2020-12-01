/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.navigation.activity.navigator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.surfstudio.android.activity.holder.ActiveActivityHolder;
import ru.surfstudio.android.core.ui.navigation.Navigator;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute;

/**
 * глобальный навигатор для перехода по экранам не имея доступ
 * к контексту активити (из слоя Interactor)
 */
@Deprecated
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
        if (activity == null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        Bundle bundle = route.prepareBundle();
        if (intent.resolveActivity(localContext.getPackageManager()) != null) {
            localContext.startActivity(intent, bundle);
            return true;
        }

        return false;
    }
}
