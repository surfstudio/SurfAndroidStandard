package ru.surfstudio.standard.interactor.common.error

/**
 * коды ошибок [HttpProtocolException]
 */
object HttpCodes {
    val CODE_200 = 200 //успех
    val CODE_304 = 304 //нет обновленных данных
    val CODE_401 = 401 //невалидный токен
    val CODE_400 = 400 //Bad request
    val CODE_403 = 403 // Доступ запрещен
    val CODE_404 = 404 // не найдено
    val CODE_500 = 500 //ошибка сервера
    val UNSPECIFIED = 0 //неопределен
}
