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

import org.jetbrains.annotations.NotNull;

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
import ru.surfstudio.android.core.ui.navigation.ActivityRouteInterface;
import ru.surfstudio.android.core.ui.navigation.ScreenResult;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute;
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute;
import ru.surfstudio.android.core.ui.navigation.activity.route.NewIntentRoute;
import ru.surfstudio.android.core.ui.navigation.event.result.BaseActivityResultDelegate;
import ru.surfstudio.android.core.ui.navigation.event.result.CrossFeatureSupportOnActivityResultRoute;
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureEvent;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstallState;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstallStatus;
import ru.surfstudio.android.core.ui.navigation.feature.installer.SplitFeatureInstaller;
import ru.surfstudio.android.core.ui.navigation.feature.route.dynamic_feature.DynamicCrossFeatureRoute;
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.ActivityCrossFeatureRoute;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;


public abstract class BaseActivityNavigator extends BaseActivityResultDelegate
        implements ActivityNavigator, NewIntentDelegate, OnCompletelyDestroyDelegate, OnResumeDelegate, OnPauseDelegate {

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
    public BaseActivityNavigator(ActivityProvider activityProvider,
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
    public BaseActivityNavigator(ActivityProvider activityProvider,
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


    @NotNull
    @Override
    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            Class<? extends SupportOnActivityResultRoute<T>> routeClass) {
        try {
            return this.observeOnActivityResult(routeClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("route class " + routeClass.getCanonicalName()
                    + "must have default constructor", e);
        }
    }


    @NotNull
    @Override
    public <T extends Serializable> Observable<ScreenResult<T>> observeResult(
            @NotNull SupportOnActivityResultRoute<T> route) {
        return super.observeOnActivityResult(route);
    }


    @Override
    public void finishCurrent() {
        activityProvider.get().finish();
    }


    public void finishAffinity() {
        ActivityCompat.finishAffinity(activityProvider.get());
    }


    @Override
    public <T extends Serializable> void finishWithResult(@NotNull ActivityWithResultRoute<T> activeScreenRoute,
                                                          boolean success) {
        finishWithResult(activeScreenRoute, null, success);
    }


    @Override
    public <T extends Serializable> void finishWithResult(@NotNull SupportOnActivityResultRoute<T> activeScreenRoute,
                                                          @NotNull T result) {
        finishWithResult(activeScreenRoute, result, true);
    }


    @Override
    public <T extends Serializable> void finishWithResult(SupportOnActivityResultRoute<T> currentScreenRoute,
                                                          T result, boolean success) {
        Intent resultIntent = currentScreenRoute.prepareResultIntent(result);
        activityProvider.get().setResult(
                success ? Activity.RESULT_OK : Activity.RESULT_CANCELED,
                resultIntent);
        finishCurrent();
    }


    @Override
    public boolean start(ActivityRoute route) {
        Context context = activityProvider.get();
        Intent intent = route.prepareIntent(context);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent, prepareBundleCompat(route));
            return true;
        }
        return false;
    }


    @NotNull
    @Override
    public Observable<SplitFeatureInstallState> start(@NotNull ActivityCrossFeatureRoute route) {
        return startCrossFeature(route, startStatusSubject -> performStart(route, startStatusSubject));
    }


    @NotNull
    @Override
    public Observable<SplitFeatureInstallState> startForResult(@NotNull CrossFeatureSupportOnActivityResultRoute route) {
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


    @Override
    public boolean startForResult(@NotNull SupportOnActivityResultRoute route) {
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


    @NotNull
    @Override
    public <T extends NewIntentRoute> Observable<T> observeNewIntent(Class<T> newIntentRouteClass) {
        try {
            return this.observeNewIntent(newIntentRouteClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("route class " + newIntentRouteClass.getCanonicalName()
                    + "must have default constructor", e);
        }
    }


    @NotNull
    @Override
    public <T extends NewIntentRoute> Observable<T> observeNewIntent(@NotNull T newIntentRoute) {
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