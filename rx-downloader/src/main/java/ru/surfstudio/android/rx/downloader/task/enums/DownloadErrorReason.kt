package ru.surfstudio.android.rx.downloader.task.enums

/**
 * Причина ошибки загрузки у [DownloadTask]
 */
enum class DownloadErrorReason {
    NOT_SPECIFIED, //не указан
    ERROR_CANNOT_RESUME, //невозможно возобновить загрузку
    ERROR_DEVICE_NOT_FOUND, //внешний накопитель не найден. При загрузке на SD-карту
    ERROR_FILE_ALREADY_EXISTS, //файл уже существует
    ERROR_FILE_ERROR, //неизвестная ошибка
    ERROR_HTTP_DATA_ERROR, //ошибка сервера
    ERROR_INSUFFICIENT_SPACE, //недостаточно памяти
    ERROR_TOO_MANY_REDIRECTS, //слишком много переадресаций
    ERROR_UNHANDLED_HTTP_CODE, //невозможно обработать HTTP код
    ERROR_UNKNOWN //неизвестная ошибка
}
