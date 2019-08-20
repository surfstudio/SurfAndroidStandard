package ru.surfstudio.android.core.ui.permission.exceptions

/**
 * Исключение, возникающее, если {@link PermissionRequest#showPermissionsRational} равен true, но
 * {@link PermissionRequest#permissionsRationalRoute} и {@link PermissionRequest#permissionsRationalStr} равны null.
 */
class PermissionsRationalIsNotProvidedException : RuntimeException(
        "showPermissionsRational is set to true, but permissionsRationalRoute and permissionsRationalStr " +
                "are null. Set one of them to non null value or set showPermissionsRational to false."
)