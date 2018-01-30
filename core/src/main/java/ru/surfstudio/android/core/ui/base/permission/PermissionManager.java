package ru.surfstudio.android.core.ui.base.permission;


import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.event.delegate.permission.result.RequestPermissionsResultDelegate;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * позволяет проверять и запрашивать Runtime Permissions
 */
public abstract class PermissionManager implements RequestPermissionsResultDelegate {
    private ActivityProvider activityProvider;

    private Map<Integer, BehaviorSubject<Boolean>> requestSubjects = new HashMap<>();

    public PermissionManager(ActivityProvider activityProvider) {
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

    public boolean check(PermissionRequest request){
        boolean result = true;
        for(String permission: request.getPermissions()){
            result = result && check(permission);
        }
        return result;
    }



    public Observable<Boolean> request(PermissionRequest request) {
        BehaviorSubject<Boolean> requestPermissionResultSubject = BehaviorSubject.create();
        int requestCode = request.getRequestCode();
        requestSubjects.put(requestCode, requestPermissionResultSubject);
        requestPermissionIfNeeded(request);
        return requestPermissionResultSubject
                .take(1)
                .doOnNext(result -> requestSubjects.remove(requestCode));
    }

    private boolean check(String permission){
        return ContextCompat.checkSelfPermission(activityProvider.get(), permission) == PERMISSION_GRANTED;
    }

    private Boolean isAllGranted(int[] grantResults) {
        boolean allGranted = true;
        for(int result : grantResults) {
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
}
