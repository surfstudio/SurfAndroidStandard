package ru.surfstudio.android.core.ui.permission

//
enum class PermissionStatus(val isGranted: Boolean) {

    GRANTED(true),

    DENIED(false),

    DENIED_FOREVER(false),

    NOT_REQUESTED(false)
}