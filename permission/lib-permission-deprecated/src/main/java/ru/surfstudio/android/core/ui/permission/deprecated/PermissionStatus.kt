package ru.surfstudio.android.core.ui.permission.deprecated

/**
 * Статус разрешения.
 *
 * @param isGranted Выдано ли разрешение.
 */
@Deprecated(
    message = "Prefer using new implementation",
    replaceWith = ReplaceWith("permission.PermissionManager")
)
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
     */
    DENIED_FOREVER(false),

    /**
     * Разрешение ещё не запрашивалось.
     */
    NOT_REQUESTED(false)
}