package ru.surfstudio.android.core.ui.permission.deprecated.exceptions

/**
 * Исключение, возникающее, если {@link PermissionRequest#showPermissionsRational} равен true, но
 * {@link PermissionRequest#permissionsRationalRoute} и {@link PermissionRequest#permissionsRationalStr} равны null.
 */
@Deprecated(
    message = "Prefer using new implementation",
    replaceWith = ReplaceWith("permission.PermissionManager")
)
class PermissionsRationalIsNotProvidedException : RuntimeException(
        "showPermissionsRational is set to true, but permissionsRationalRoute and permissionsRationalStr " +
                "are null. Set one of them to non null value or set showPermissionsRational to false."
)