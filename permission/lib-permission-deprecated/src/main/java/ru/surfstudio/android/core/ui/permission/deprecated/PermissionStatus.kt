package ru.surfstudio.android.core.ui.permission.deprecated

/**
 * Статус разрешения.
 *
 * @param isGranted Выдано ли разрешение.
 */
@Deprecated("Prefer using new implementation")
enum class PermissionStatus(val isGranted: Boolean) {

    /**
     * Разрешение выдано.
     */
    GRANTED(true),

    /**
     * При предыдущем запросе в разрешении было отказано.
     */
    DENIED(false),

    /**
     * При предыдущем запросе в разрешении было отказано и выбрана опция "Don't ask again".
     * Также данный флаг может быть получен, если пользователь выбрал запрашивать только One time permissions
     */
    DENIED_FOREVER_OR_ONE_TIME_PERMISSION(false),

    /**
     * Разрешение ещё не запрашивалось.
     */
    NOT_REQUESTED(false)
}