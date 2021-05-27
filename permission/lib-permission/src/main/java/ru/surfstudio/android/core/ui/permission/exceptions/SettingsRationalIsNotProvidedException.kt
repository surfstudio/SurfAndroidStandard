package ru.surfstudio.android.core.ui.permission.exceptions

/**
 * Исключение, возникающее, если {@link PermissionRequest#settingsRationalStr} равен null.
 */
class SettingsRationalIsNotProvidedException : RuntimeException("Set settingsRationalStr to non null value.")