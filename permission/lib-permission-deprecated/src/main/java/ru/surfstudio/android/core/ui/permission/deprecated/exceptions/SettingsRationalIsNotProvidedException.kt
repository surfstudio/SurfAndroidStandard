package ru.surfstudio.android.core.ui.permission.deprecated.exceptions

/**
 * Исключение, возникающее, если {@link PermissionRequest#settingsRationalStr} равен null.
 */
@Deprecated("Prefer using new implementation")
class SettingsRationalIsNotProvidedException : RuntimeException("Set settingsRationalStr to non null value.")