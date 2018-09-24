package ru.surfstudio.android.core.ui.permission

/**
 * Исключение, возникающее, если {@link PermissionRequest#showPermissionsRational} равен true, но
 * {@link PermissionRequest#permissionsRationalRoute} и {@link PermissionRequest#permissionsRationalStr}
 * равны null.
 */
class RationalIsNotProvidedException : RuntimeException(
        "showPermissionsRational is set to true, but permissionsRationalRoute and permissionsRationalStr " +
                "are null. Override one of them or set showPermissionsRational to false."
)