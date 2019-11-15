package ru.surfstudio.standard.i_network.network.response


/**
 * Базовый интерфейс ответа
 */
abstract class BaseErrorObjResponse<Obj: BaseErrorObj>: BaseResponse {

    abstract val errorObj: BaseErrorObj?
}