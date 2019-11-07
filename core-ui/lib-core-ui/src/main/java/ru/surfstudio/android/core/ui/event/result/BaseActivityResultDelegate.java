/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev, Maxim Smirnov.

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
package ru.surfstudio.android.core.ui.event.result;


import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.surfstudio.android.core.ui.navigation.ScreenResult;
import ru.surfstudio.android.core.ui.navigation.ScreenResultNoData;
import ru.surfstudio.android.logger.Logger;

/**
 * базовый класс делегата, позволяющий регистрировать обработчики на событие onActivityResult
 */
public class BaseActivityResultDelegate implements ActivityResultDelegate {

    private SparseArray<ActivityResultRegistration> activityResultSubjects = new SparseArray<>();
    private SparseArray<ActivityResultNoDataRegistration> activityResultNoDataSubjects = new SparseArray<>();

    @SuppressWarnings("unchecked")
    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean result = false;

        int code;
        for (int i = 0; i < activityResultSubjects.size(); i++) {
            code = activityResultSubjects.keyAt(i);
            Pair<Integer, ActivityResultRegistration> routePair = new Pair<>(code, activityResultSubjects.get(code));
            if (routePair.first != null
                    && routePair.first == requestCode
                    && routePair.second != null) {
                Subject resultSubject = routePair.second.getSubject();
                resultSubject.onNext(
                        new ScreenResult(
                                resultCode == Activity.RESULT_OK,
                                data != null ? ((SupportOnActivityResultRoute) routePair.second.getRoute())
                                        .parseResultIntent(data) : null
                        )
                );
                result = true;
            }
        }

        for (int i = 0; i < activityResultNoDataSubjects.size(); i++) {
            code = activityResultNoDataSubjects.keyAt(i);
            Pair<Integer, ActivityResultNoDataRegistration> routePair = new Pair<>(code, activityResultNoDataSubjects.get(code));
            if (routePair.first != null
                    && routePair.first == requestCode
                    && routePair.second != null) {
                Subject resultSubject = routePair.second.getSubject();
                resultSubject.onNext(
                        new ScreenResultNoData(resultCode == Activity.RESULT_OK)
                );
                result = true;
            }
        }

        return result;
    }

    protected boolean isObserved(SupportCodeActivityRoute route) {
        final int code = route.getRequestCode();
        return activityResultSubjects.indexOfKey(code) != -1
                || activityResultNoDataSubjects.indexOfKey(code) != -1;
    }

    @NonNull
    protected <T extends Serializable> Observable<ScreenResult<T>> observeOnActivityResult(SupportOnActivityResultRoute<T> route) {
        tryRemoveDuplicateResultSubjects(route);
        PublishSubject<ScreenResult<T>> resultSubject = PublishSubject.create();
        activityResultSubjects.put(route.getRequestCode(), new ActivityResultRegistration<>(route, resultSubject));
        return resultSubject;
    }

    @NonNull
    protected Observable<ScreenResultNoData> observeOnActivityResultNoData(SupportOnActivityResultNoDataRoute route) {
        tryRemoveDuplicateResultSubjects(route);
        PublishSubject<ScreenResultNoData> resultSubject = PublishSubject.create();
        activityResultNoDataSubjects.put(route.getRequestCode(), new ActivityResultNoDataRegistration(route, resultSubject));
        return resultSubject;
    }

    private void tryRemoveDuplicateResultSubjects(SupportCodeActivityRoute route) {
        Set<Integer> toDeleteKeys = new HashSet<>();
        Set<Pair<Integer, BaseActivityResultRegistration>> registrations = new HashSet<>();

        int code;
        for (int i = 0; i < activityResultSubjects.size(); i++) {
            code = activityResultSubjects.keyAt(i);
            registrations.add(new Pair<>(code, activityResultSubjects.get(code)));
        }
        for (int i = 0; i < activityResultNoDataSubjects.size(); i++) {
            code = activityResultNoDataSubjects.keyAt(i);
            registrations.add(new Pair<>(code, activityResultNoDataSubjects.get(code)));
        }
        for (Pair<Integer, BaseActivityResultRegistration> registeredRoutePair : registrations) {
            if (registeredRoutePair.first != null
                    && registeredRoutePair.first == route.getRequestCode()
                    && registeredRoutePair.second != null) {
                registeredRoutePair.second.getSubject().onComplete();
                toDeleteKeys.add(registeredRoutePair.first);
                Logger.v(this.getClass().getName(), "duplicate registered SupportOnActivityResultRoute: "
                        + registeredRoutePair.second.getRoute() + " old route unregistered");
            }
        }
        //удаляем ключи здесь, чтобы не было ConcurrentModificationException
        for (Integer key : toDeleteKeys) {
            activityResultSubjects.remove(key);
            activityResultNoDataSubjects.remove(key);
        }
    }

    private abstract class BaseActivityResultRegistration<S, R extends SupportCodeActivityRoute> {

        @NonNull
        protected R route;

        @NonNull
        protected PublishSubject<S> subject;

        protected BaseActivityResultRegistration(@NonNull R route, @NonNull
                PublishSubject<S> subject) {
            this.route = route;
            this.subject = subject;
        }

        @NonNull
        public R getRoute() {
            return route;
        }

        @NonNull
        public PublishSubject<S> getSubject() {
            return subject;
        }
    }

    private class ActivityResultRegistration<T extends Serializable>
            extends BaseActivityResultRegistration<ScreenResult<T>, SupportOnActivityResultRoute<T>> {

        public ActivityResultRegistration(SupportOnActivityResultRoute<T> route, PublishSubject<ScreenResult<T>> subject) {
            super(route, subject);
        }
    }


    private class ActivityResultNoDataRegistration
            extends BaseActivityResultRegistration<ScreenResultNoData, SupportOnActivityResultNoDataRoute> {

        public ActivityResultNoDataRegistration(SupportOnActivityResultNoDataRoute route, PublishSubject<ScreenResultNoData> subject) {
            super(route, subject);
        }
    }
}