package ru.surfstudio.android.core.app.interactor.common.network.error;

/**
 * коды ошибок
 */
public class HttpCodes {

    public static final int CODE_200 = 200; //успех
    public static final int CODE_304 = 304; //нет обновленных данных
    public static final int CODE_401 = 401; //невалидный токен
    public static final int CODE_400 = 400; //Bad request
    public static final int CODE_403 = 403; // Доступ запрещен
    public static final int CODE_404 = 404;
    public static final int CODE_500 = 500; //ошибка сервера
    public static final int UNSPECIFIED = 0; //неопределен

    private HttpCodes() {
        //non-instantiate
    }
}
