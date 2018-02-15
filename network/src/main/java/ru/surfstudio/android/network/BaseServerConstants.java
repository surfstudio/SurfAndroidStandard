package ru.surfstudio.android.network;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BaseServerConstants {

    public static final String HEADER_QUERY_MODE = "queryMode";
    public static final int QUERY_MODE_ONLY_IF_CHANGED = 1; //используется механизм etag
    public static final int QUERY_MODE_FORCE = 2; //получить данные с сервера принудительно
    public static final int QUERY_MODE_FROM_SIMPLE_CACHE = 3; //получить данные из кеша

    @IntDef({QUERY_MODE_ONLY_IF_CHANGED,
            QUERY_MODE_FORCE,
            QUERY_MODE_FROM_SIMPLE_CACHE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface QueryMode {
    }
}
