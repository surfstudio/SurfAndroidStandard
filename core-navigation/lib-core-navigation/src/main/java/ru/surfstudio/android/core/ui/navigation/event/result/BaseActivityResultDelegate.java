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
package ru.surfstudio.android.core.ui.navigation.event.result;


import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.surfstudio.android.core.ui.event.result.ActivityResultDelegate;
import ru.surfstudio.android.core.ui.navigation.ScreenResult;
import ru.surfstudio.android.logger.Logger;

/**
 * базовый класс делегата, позволяющий регистрировать обработчики на событие onActivityResult
 */
@Deprecated
public class BaseActivityResultDelegate implements ActivityResultDelegate {
    private Map<Integer, ActivityResultRegistration> activityResultSubjects = new HashMap<>();

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Map.Entry<Integer, ActivityResultRegistration> routeEntry : activityResultSubjects.entrySet()) {
            if (routeEntry.getKey() == requestCode) {
                Subject resultSubject = routeEntry.getValue().getSubject();
                resultSubject.onNext(new ScreenResult<>(
                        resultCode == Activity.RESULT_OK,
                        data != null ? routeEntry.getValue().getRoute().parseResultIntent(data) : null));
                return true;
            }
        }
        return false;
    }

    protected <T extends Serializable> Observable<ScreenResult<T>> observeOnActivityResult(
            SupportOnActivityResultRoute<T> route) {
        tryRemoveDuplicateResultSubjects(route);
        PublishSubject<ScreenResult<T>> resultSubject = PublishSubject.create();
        activityResultSubjects.put(route.getRequestCode(), new ActivityResultRegistration(route, resultSubject));
        return resultSubject;
    }

    protected boolean isObserved(SupportOnActivityResultRoute route) {
        for (Integer registeredRequestCode : activityResultSubjects.keySet()) {
            if (registeredRequestCode == route.getRequestCode()) {
                return true;
            }
        }
        return false;
    }

    private void tryRemoveDuplicateResultSubjects(SupportOnActivityResultRoute route) {
        Set<Integer> toDeleteKeys = new HashSet<>();
        for (Map.Entry<Integer, ActivityResultRegistration> registeredRouteEntry : activityResultSubjects.entrySet()) {
            if (registeredRouteEntry.getKey() == route.getRequestCode()) {
                registeredRouteEntry.getValue().getSubject().onComplete();
                toDeleteKeys.add(registeredRouteEntry.getKey());
                Logger.v(this.getClass().getName(), "duplicate registered SupportOnActivityResultRoute: "
                        + registeredRouteEntry.getValue().getRoute() + " old route unregistered");
            }
        }
        //удаляем ключи здесь, чтобы не было ConcurrentModificationException
        for (Integer key : toDeleteKeys) {
            activityResultSubjects.remove(key);
        }
    }

    private class ActivityResultRegistration<T extends Serializable> {
        private SupportOnActivityResultRoute<T> route;
        private PublishSubject<ScreenResult<T>> subject;

        public ActivityResultRegistration(SupportOnActivityResultRoute<T> route, PublishSubject<ScreenResult<T>> subject) {
            this.route = route;
            this.subject = subject;
        }

        public SupportOnActivityResultRoute<T> getRoute() {
            return route;
        }

        public PublishSubject<ScreenResult<T>> getSubject() {
            return subject;
        }
    }


}