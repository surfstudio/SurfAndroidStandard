package ru.surfstudio.android.core.ui.base.permission;

/**
 * базовый класс запроса Runtime Permissions
 */
public abstract class PermissionRequest {

    private static final int MAX_REQUEST_CODE = 32768;

    public abstract String[] getPermissions();

    public int getRequestCode(){
        return (this.getClass().getCanonicalName().hashCode() & 0x7fffffff) % MAX_REQUEST_CODE;
    }
}
