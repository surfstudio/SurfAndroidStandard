/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.newintent.NewIntentDelegate;
import ru.surfstudio.android.core.ui.event.result.BaseActivityResultDelegate;
import ru.surfstudio.android.core.ui.navigation.Navigator;
import ru.surfstudio.android.core.ui.navigation.ScreenResult;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute;
import ru.surfstudio.android.core.ui.navigation.activity.route.NewIntentRoute;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;

/**
 * позволяет осуществлять навигацияю между активити
 * <p>
 * !!!В случае конфликтов возвращения результата между несколькими инстансами навигаторами
 * можно рассмотреть добавление к RequestCode хеша от имени экрана контейнера
 * Конфликт может возникнуть при открытии одинаковых экранов из, например, кастомной вью с
 * презентером и родительской активити вью
 */
public abstract class ActivityNavigator extends BaseActivityResultDelegate
        implements Navigator, NewIntentDelegate {

    private Map<NewIntentRoute, Subject> newIntentSubjects = new HashMap<>();
    private final ActivityProvider activityProvider;


    public ActivityNavigator(ActivityProvider activityProvider,
                             ScreenEventDelegateManager eventDelegateManager) {
        eventDelegateManager.registerDelegate(this);
        this.activityProvider = activityProvider;
    }

    protected abstract void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle bundle);

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param routeClass класс маршрута экрана, который должен вернуть результат
     * @param <T>        тип возвращаемых данных
     */
    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            Class<? extends ActivityWithResultRoute<T>> routeClass) {
        try {
            return this.observeOnActivityResult(routeClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("route class " + routeClass.getCanonicalName()
                    + "must have default constructor", e);
        }
    }

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param route маршрут экрана, который должен вернуть результат
     * @param <T>   тип возвращаемых данных
     */
    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            ActivityWithResultRoute route) {
        return super.observeOnActivityResult(route);
    }

    /**
     * Закрываает текущую активити
     */
    public void finishCurrent() {
        activityProvider.get().finish();
    }

    /**
     * Закрываает текущую активити Affinity
     */
    public void finishAffinity() {
        ActivityCompat.finishAffinity(activityProvider.get());
    }

    /**
     * Закрываает текущую активити c результатом
     *
     * @param activeScreenRoute маршрут текущего экрана
     * @param success           показывает успешное ли завершение
     * @param <T>               тип возвращаемого значения
     */
    public <T extends Serializable> void finishWithResult(ActivityWithResultRoute<T> activeScreenRoute,
                                                          boolean success) {
        finishWithResult(activeScreenRoute, null, success);
    }

    /**
     * Закрываает текущую активити c результатом
     *
     * @param activeScreenRoute маршрут текущего экрана
     * @param result            возвращаемый результат
     * @param <T>               тип возвращаемого значения
     */
    public <T extends Serializable> void finishWithResult(ActivityWithResultRoute<T> activeScreenRoute,
                                                          T result) {
        finishWithResult(activeScreenRoute, result, true);
    }

    /**
     * Закрываает текущую активити c результатом
     *
     * @param currentScreenRoute маршрут текущего экрана
     * @param result             возвращаемый результат
     * @param success            показывает успешное ли завершение
     * @param <T>                тип возвращаемого значения
     */
    public <T extends Serializable> void finishWithResult(ActivityWithResultRoute<T> currentScreenRoute,
                                                          T result, boolean success) {
        Intent resultIntent = currentScreenRoute.prepareResultIntent(result);
        activityProvider.get().setResult(
                success ? Activity.RESULT_OK : Activity.RESULT_CANCELED,
                resultIntent);
        finishCurrent();
    }

    /**
     * Запуск активити.
     *
     * @param route роутер
     * @return {@code true} если активити успешно запущен, иначе {@code false}
     */
    public boolean start(ActivityRoute route) {
        Context context = activityProvider.get();
        Intent intent = route.prepareIntent(context);
        Bundle bundle = route.prepareBundle();
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent, bundle);
            return true;
        }

        return false;
    }

    /**
     * Запуск активити.
     *
     * @param route роутер
     * @return {@code true} если активити успешно запущен, иначе {@code false}
     */
    public boolean startForResult(ActivityWithResultRoute route) {
        if (!super.isObserved(route)) {
            throw new IllegalStateException("route class " + route.getClass().getSimpleName()
                    + " must be registered by method ActivityNavigator#observeResult");
        }

        Context context = activityProvider.get();
        Intent intent = route.prepareIntent(context);
        Bundle bundle = route.prepareBundle();
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(intent, route.getRequestCode(), bundle);
            return true;
        }

        return false;
    }

    // =========================  NEW INTENT =================================

    @Override
    public boolean onNewIntent(Intent intent) {
        for (NewIntentRoute route : newIntentSubjects.keySet()) {
            if (route.parseIntent(intent)) {
                Subject resultSubject = newIntentSubjects.get(route);
                resultSubject.onNext(route);
                return true;
            }
        }
        return false;
    }

    /**
     * позволяет подписываться на событие OnNewIntent
     *
     * @param newIntentRouteClass класс, отвечающий за парсинг intent
     */
    public <T extends NewIntentRoute> Observable<T> observeNewIntent(Class<T> newIntentRouteClass) {
        try {
            return this.observeNewIntent(newIntentRouteClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("route class " + newIntentRouteClass.getCanonicalName()
                    + "must have default constructor", e);
        }
    }

    /**
     * позволяет подписываться на событие OnNewIntent
     *
     * @param newIntentRoute отвечает за парсинг intent
     */
    public <T extends NewIntentRoute> Observable<T> observeNewIntent(T newIntentRoute) {
        tryRemoveDuplicateNewIntentEventSubjects(newIntentRoute);
        PublishSubject<T> eventSubject = PublishSubject.create();
        newIntentSubjects.put(newIntentRoute, eventSubject);
        return eventSubject;
    }

    private void tryRemoveDuplicateNewIntentEventSubjects(NewIntentRoute eventParser) {
        for (NewIntentRoute registeredRoute : newIntentSubjects.keySet()) {
            if (registeredRoute.getClass().getCanonicalName().equals(eventParser.getClass().getCanonicalName())) {
                newIntentSubjects.get(registeredRoute).onComplete();
                newIntentSubjects.remove(registeredRoute);
                Log.v(this.getClass().getName(), "duplicate registered NewIntentRoute :"
                        + registeredRoute + " old route unregistered");
            }
        }
    }
}