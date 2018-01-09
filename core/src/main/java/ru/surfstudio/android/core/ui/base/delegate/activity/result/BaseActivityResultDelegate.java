package ru.surfstudio.android.core.ui.base.delegate.activity.result;


import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.surfstudio.android.core.app.log.Logger;
import ru.surfstudio.android.core.ui.base.navigation.ScreenResult;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * базовый класс делегата, позволяющий регистрировать обработчики на событие onActivityResult
 */
public class BaseActivityResultDelegate implements ActivityResultDelegate {
    private Map<Integer, ActivityResultRegistration> activityResultSubjects = new HashMap<>();

    @Data
    @AllArgsConstructor
    private class ActivityResultRegistration<T extends Serializable> {
        private  SupportOnActivityResultRoute<T> route;
        private PublishSubject<ScreenResult<T>> subject;
    }

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
                registeredRouteEntry.getValue().getSubject().onCompleted();
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


}
