package ru.surfstudio.android.core.ui.base.permission;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.util.SdkUtils;
import ru.surfstudio.android.core.ui.base.screen.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.event.result.RequestPermissionsResultDelegate;

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
     * @param request
     * @return выдано ли разрешение
     */
    public boolean check(PermissionRequest request){
        boolean result = true;
        for (String permission : request.getPermissions()) {
            result = result && check(permission);
        }
        return result;
    }

    /**
     * запрашивает разрешение
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

    /**
     * Проверка условия, должен ли UI показать пояснение, для чего нужен запрашиваемый Permission.
     *
     * @return true/false
     */
    public boolean shouldShowRequestPermissionRationale(PermissionRequest permission) {
        if (SdkUtils.isAtLeastMarshmallow()) {
            return checkRequestPermissionRationale(permission);
        } else {
            return false;
        }
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