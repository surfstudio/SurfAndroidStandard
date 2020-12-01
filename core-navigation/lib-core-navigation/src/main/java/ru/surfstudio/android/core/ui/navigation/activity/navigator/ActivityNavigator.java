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
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.agna.ferro.rx.ObservableOperatorFreeze;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseDelegate;
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeDelegate;
import ru.surfstudio.android.core.ui.event.newintent.NewIntentDelegate;
import ru.surfstudio.android.core.ui.navigation.event.result.BaseActivityResultDelegate;
import ru.surfstudio.android.core.ui.navigation.event.result.CrossFeatureSupportOnActivityResultRoute;
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute;
import ru.surfstudio.android.core.ui.navigation.ActivityRouteInterface;
import ru.surfstudio.android.core.ui.navigation.Navigator;
import ru.surfstudio.android.core.ui.navigation.ScreenResult;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute;
import ru.surfstudio.android.core.ui.navigation.activity.route.NewIntentRoute;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureEvent;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstallState;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstallStatus;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstaller;
import ru.surfstudio.android.core.ui.navigation.feature.route.dynamic_feature.DynamicCrossFeatureRoute;
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.ActivityCrossFeatureRoute;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;

/**
 * позволяет осуществлять навигацияю между активити
 * <p>
 * !!!В случае конфликтов возвращения результата между несколькими инстансами навигаторами
 * можно рассмотреть добавление к RequestCode хеша от имени экрана контейнера
 * Конфликт может возникнуть при открытии одинаковых экранов из, например, кастомной вью с
 * презентером и родительской активити вью
 */
@Deprecated
public abstract class ActivityNavigator extends BaseActivityResultDelegate
        implements Navigator, NewIntentDelegate, OnCompletelyDestroyDelegate, OnResumeDelegate, OnPauseDelegate {

    private Map<NewIntentRoute, Subject> newIntentSubjects = new HashMap<>();
    private final ActivityProvider activityProvider;
    private final SplitFeatureInstaller splitFeatureInstaller;
    private Disposable splitFeatureInstallDisposable = Disposables.disposed();
    private final Boolean isSplitFeatureModeOn;
    private final BehaviorSubject<Boolean> freezeSelector = BehaviorSubject.createDefault(false);

    private interface OnFeatureInstallListener {
        void doOnInstall(BehaviorSubject<SplitFeatureInstallState> startStatusSubject);
    }

    /**
     * Base activity navigator constructor.
     *
     * @param activityProvider     actual Activity provider instance
     * @param eventDelegateManager screen event delegate manager instance
     */
    public ActivityNavigator(ActivityProvider activityProvider,
                             ScreenEventDelegateManager eventDelegateManager) {
        this(activityProvider, eventDelegateManager, null, false);
    }

    /**
     * Activity navigator with "split-features" support constructor.
     *
     * @param activityProvider      actual Activity provider instance
     * @param eventDelegateManager  screen event delegate manager instance
     * @param splitFeatureInstaller "split-feature" install manager
     * @param isSplitFeatureModeOn  "split-feature" navigation activation flag.
     *                              Actually, it needs to be {@code true} just for release build which
     *                              has already been deployed to Google Play. Otherwise, cross-feature
     *                              navigation won't work at all.
     */
    public ActivityNavigator(ActivityProvider activityProvider,
                             ScreenEventDelegateManager eventDelegateManager,
                             SplitFeatureInstaller splitFeatureInstaller,
                             Boolean isSplitFeatureModeOn) {
        eventDelegateManager.registerDelegate(this);
        this.activityProvider = activityProvider;
        this.splitFeatureInstaller = splitFeatureInstaller;
        this.isSplitFeatureModeOn = isSplitFeatureModeOn;
    }

    protected abstract void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle bundle);

    @Override
    public void onCompletelyDestroy() {
        splitFeatureInstallDisposable.dispose();
    }

    @Override
    public void onResume() {
        freezeSelector.onNext(false);
    }

    @Override
    public void onPause() {
        freezeSelector.onNext(true);
    }

    /**
     * позволяет подписываться на событие OnActivityResult
     *
     * @param routeClass класс маршрута экрана, который должен вернуть результат
     * @param <T>        тип возвращаемых данных
     */
    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            Class<? extends SupportOnActivityResultRoute<T>> routeClass) {
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
            SupportOnActivityResultRoute route) {
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
    public <T extends Serializable> void finishWithResult(SupportOnActivityResultRoute<T> activeScreenRoute,
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
    public <T extends Serializable> void finishWithResult(SupportOnActivityResultRoute<T> currentScreenRoute,
                                                          T result, boolean success) {
        Intent resultIntent = currentScreenRoute.prepareResultIntent(result);
        activityProvider.get().setResult(
                success ? Activity.RESULT_OK : Activity.RESULT_CANCELED,
                resultIntent);
        finishCurrent();
    }

    /**
     * Launch a new activity.
     * <p>
     * Works synchronically.
     *
     * @param route navigation route
     * @return {@code true} if activity started successfully, {@code false} otherwise
     */
    public boolean start(ActivityRoute route) {
        Context context = activityProvider.get();
        Intent intent = route.prepareIntent(context);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent, prepareBundleCompat(route));
            return true;
        }
        return false;
    }

    /**
     * Launch a new activity from another Feature Module.
     * <p>
     * Performs asynchronically due to type of the target Feature Module.
     * This method returns stream of install state change events. You can make a subscription in
     * your Presenter and handle errors or any other type of events during Dynamic Feature
     * installation.
     *
     * @param route navigation route
     * @return stream of install state change events
     */
    public Observable<SplitFeatureInstallState> start(ActivityCrossFeatureRoute route) {
        return startCrossFeature(route, startStatusSubject -> performStart(route, startStatusSubject));
    }

    /**
     * Launch a new Activity for result from another Feature Module.
     * <p>
     * Performs asynchronously due to type of the target Feature Module.
     * This method returns stream of install state change events. You can make a subscription in
     * your Presenter and handle errors or any other type of events during Dynamic Feature
     * installation.
     *
     * @param route navigation route
     * @return stream of install state change events
     */
    public Observable<SplitFeatureInstallState> startForResult(CrossFeatureSupportOnActivityResultRoute route) {
        return startCrossFeature(route, startStatusSubject -> performStartForResult(route, startStatusSubject));
    }

    private Observable<SplitFeatureInstallState> startCrossFeature(
            ActivityRouteInterface route,
            OnFeatureInstallListener onFeatureInstallListener
    ) {
        BehaviorSubject<SplitFeatureInstallState> startStatusSubject = BehaviorSubject.create();
        if (route instanceof DynamicCrossFeatureRoute && this.isSplitFeatureModeOn) {
            DynamicCrossFeatureRoute dynamicCrossFeatureRoute = (DynamicCrossFeatureRoute) route;
            splitFeatureInstallDisposable.dispose();
            splitFeatureInstallDisposable =
                    splitFeatureInstaller.installFeature(dynamicCrossFeatureRoute.splitNames())
                            .lift(new ObservableOperatorFreeze<>(freezeSelector))
                            .subscribe(splitFeatureInstallState -> {
                                if (splitFeatureInstallState.getInstallEvent() instanceof SplitFeatureEvent.InstallationStateEvent.Installed) {
                                    startStatusSubject.onNext(splitFeatureInstallState);
                                    onFeatureInstallListener.doOnInstall(startStatusSubject);
                                }
                            });
        } else {
            onFeatureInstallListener.doOnInstall(startStatusSubject);
        }
        return startStatusSubject.hide();
    }

    private void performStart(ActivityRoute route, BehaviorSubject<SplitFeatureInstallState> startStatusSubject) {
        boolean startupStatus = start(route);
        emitFeatureInstallState(startStatusSubject, startupStatus);
    }

    private void performStartForResult(SupportOnActivityResultRoute route, BehaviorSubject<SplitFeatureInstallState> startStatusSubject) {
        boolean startupStatus = startForResult(route);
        emitFeatureInstallState(startStatusSubject, startupStatus);
    }

    private void emitFeatureInstallState(BehaviorSubject<SplitFeatureInstallState> splitFeatureInstallStateSubject, boolean startupStatus) {
        SplitFeatureInstallStatus status = SplitFeatureInstallStatus.Companion.getByValue(startupStatus);
        splitFeatureInstallStateSubject.onNext(new SplitFeatureInstallState(status));
    }

    /**
     * Launch a new activity for result.
     *
     * @param route navigation route
     * @return {@code true} if activity started successfully, {@code false} otherwise
     */
    public boolean startForResult(SupportOnActivityResultRoute route) {
        if (!super.isObserved(route)) {
            throw new IllegalStateException("route class " + route.getClass().getSimpleName()
                    + " must be registered by method ActivityNavigator#observeResult");
        }
        Context context = activityProvider.get();
        Intent intent = route.prepareIntent(context);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivityForResult(intent, route.getRequestCode(), prepareBundleCompat(route));
            return true;
        }
        return false;
    }

    /**
     * Bundle preparation backward compatible implementation.
     * Handles getting bundle from both {@link ActivityRoute#prepareActivityOptionsCompat()} and
     * {@link ActivityRoute#prepareBundle()} methods in the specified priority order.
     *
     * @param route activity route
     * @return actual bundle
     */
    private Bundle prepareBundleCompat(ActivityRouteInterface route) {
        Bundle bundle = null;
        ActivityOptionsCompat activityOptionsCompat = route.prepareActivityOptionsCompat();
        if (activityOptionsCompat != null) {
            bundle = route.prepareActivityOptionsCompat().toBundle();
        }
        //noinspection deprecation
        Bundle deprecatedBundle = route.prepareBundle();
        if (bundle != null) {
            return bundle;
        }
        return deprecatedBundle;
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