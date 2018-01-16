package ru.surfstudio.android.core.app.interactor.common.network;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ServerConstants {

    public static final String HEADER_QUERY_MODE = "queryMode";
    public static final int QUERY_MODE_ONLY_IF_CHANGED = 1; //используется механизм etag
    public static final int QUERY_MODE_FORCE = 2; //получить данные с сервера принудительно
    public static final int QUERY_MODE_FROM_SIMPLE_CACHE = 3; //получить данные из кеша
    /**
     * используется как статический хедер для дополнительных запрсов простого кеша с единственным элементом
     * может использоваться для всех запросов кроме GET, задается аннотацией к методу апи
     */
    public static final String HEADER_QUERY_MODE_FORCE_FULL = HEADER_QUERY_MODE + ":" + QUERY_MODE_FORCE;
    public static final String KEY_TOKEN = "token";
    public static final String SUCCESS_PAYMENT = "succeess=1";
    public static final String ERROR_PAYMENT = "succeess=0";

    @IntDef({QUERY_MODE_ONLY_IF_CHANGED,
            QUERY_MODE_FORCE,
            QUERY_MODE_FROM_SIMPLE_CACHE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface QueryMode {
    }
}
