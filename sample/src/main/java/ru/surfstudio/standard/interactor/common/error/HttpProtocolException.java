package ru.surfstudio.standard.interactor.common.error;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import ru.surfstudio.standard.interactor.common.response.ResultResponse;
import timber.log.Timber;

/**
 * получен ответ не 2xx
 */
@Data
public class HttpProtocolException extends NetworkException {

    private final int code;
    private final String httpMessage;
    private final int innerCode; //дополнительный внутренний код срвера
    private final String developerMessage;
    private final Map<String, Object> result; // некоторые ошибки имеют результат (лол)


    public HttpProtocolException(HttpException cause, String httpMessage, int code, String url) {
        super(prepareMessage(httpMessage, code, url, null, 0), cause);
        this.code = code;
        this.httpMessage = httpMessage;

        ResponseBody responseBody = cause.response().errorBody();
        ResultResponse<Map<String, Object>> response = getFromError(responseBody, ResultResponse.class);
        if (response != null) {
            innerCode = response.getErrorCode();
            developerMessage = response.getUserMessage();
            result = response.getResult();
        } else {
            innerCode = -1;
            developerMessage = null;
            result = null;
        }
    }

    private static String prepareMessage(String httpMessage, int code, String url, String developerMessage, int innerCode) {
        return " httpCode=" + code + "\n" +
                ", httpMessage='" + httpMessage + "'" +
                ", url='" + url + "'" + "\n" +
                ", innerCode=" + innerCode +
                ", developerMessage='" + developerMessage + "'";
    }

    private <T> T getFromError(ResponseBody responseBody, Class<T> type) {
        String jsonString = null;
        try {
            jsonString = responseBody.string();
        } catch (IOException e) {
            Timber.w(e, "Не возможно распарсить ответ сервера об ошибке");
        }
        Gson gson = new Gson();
        return gson.fromJson(jsonString, type);
    }
}
