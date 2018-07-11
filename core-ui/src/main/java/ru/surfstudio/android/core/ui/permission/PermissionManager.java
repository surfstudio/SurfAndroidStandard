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
package ru.surfstudio.android.core.ui.permission;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.event.result.RequestPermissionsResultDelegate;
import ru.surfstudio.android.core.ui.provider.ActivityProvider;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * позволяет проверять и запрашивать Runtime Permissions
 */
public abstract class PermissionManager implements RequestPermissionsResultDelegate {
    private ActivityProvider activityProvider;

    private Map<Integer, BehaviorSubject<Boolean>> requestSubjects = new HashMap<>();

    public PermissionManager(ActivityProvider activityProvider,
                             ScreenEventDelegateManager eventDelegateManager) {
        eventDelegateManager.registerDelegate(this);
        this.activityProvider = activityProvider;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestSubjects.containsKey(requestCode)) {
            requestSubjects.get(requestCode).onNext(isAllGranted(grantResults));
            return true;
        } else {
            return false;
        }
    }

    protected abstract void requestPermission(PermissionRequest request);

    /**
     * проверяет наличие разрешений без запрашивания RuntimePermission
     *
     * @param request
     * @return выдано ли разрешение
     */
    public boolean check(PermissionRequest request) {
        boolean result = true;
        for (String permission : request.getPermissions()) {
            result = result && check(permission);
        }
        return result;
    }

    public Observable<Boolean> checkObservable(PermissionRequest request) {
        return Observable.just(check(request));
    }

    /**
     * Проверка условия, должен ли UI показать пояснение, для чего нужен запрашиваемый Permission.
     * @return true/false
     *
     * Это один из необходимых флагов для понимания, что стандартный диалог отключен к показу
     * Дальнейшее использование можно посмотреть в AVS DownloadHelper.java
     * see <a href="https://clck.ru/DfZwD">
     */
    public boolean shouldShowRequestPermissionRationale(PermissionRequest permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkRequestPermissionRationale(permission);
        } else {
            return false;
        }
    }

    /**
     * запрашивает разрешение
     *
     * @param request
     * @return Observable, эмитящий событие о том, выдано ли разрешение
     */
    public Observable<Boolean> request(PermissionRequest request) {
        BehaviorSubject<Boolean> requestPermissionResultSubject = BehaviorSubject.create();
        int requestCode = request.getRequestCode();
        requestSubjects.put(requestCode, requestPermissionResultSubject);
        requestPermissionIfNeeded(request);
        return requestPermissionResultSubject
                .take(1)
                .doOnNext(result -> requestSubjects.remove(requestCode));
    }

    private boolean check(String permission) {
        return ContextCompat.checkSelfPermission(activityProvider.get(), permission) == PERMISSION_GRANTED;
    }

    private Boolean isAllGranted(int[] grantResults) {
        boolean allGranted = true;
        for (int result : grantResults) {
            allGranted = allGranted && result == PERMISSION_GRANTED;
        }
        return allGranted;
    }

    private void requestPermissionIfNeeded(PermissionRequest request) {
        if (!check(request)) {
            requestPermission(request);
        } else {
            requestSubjects.get(request.getRequestCode()).onNext(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkRequestPermissionRationale(PermissionRequest request) {
        boolean result = false;
        boolean currentPermissionStatus;

        for (String permission : request.getPermissions()) {
            currentPermissionStatus = activityProvider.get().shouldShowRequestPermissionRationale(permission);
            result = result || currentPermissionStatus;
        }
        return result;
    }
}