package ru.surfstudio.android.core.ui.permission.deprecated.exceptions

/**
 * Исключение, возникающее, если {@link PermissionRequest#settingsRationalStr} равен null.
 */
@Deprecated(
    message = "Prefer using new implementation",
    replaceWith = ReplaceWith("permission.PermissionManager")
)
class SettingsRationalIsNotProvidedException : RuntimeException("Set settingsRationalStr to non null value.")