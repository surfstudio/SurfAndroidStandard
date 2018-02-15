package ru.surfstudio.android.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Класс для описания базовой части Url сервера api
 */

public class BaseUrl {
    private static final String START_SLASH = "^/";
    private static final String END_SLASH = "/$";
    @NonNull
    private String base;
    @Nullable
    private String apiVersion;

    public BaseUrl(@NonNull String base, @Nullable String apiVersion) {
        this.base = base.replaceFirst(END_SLASH, "");
        if (apiVersion != null) {
            this.apiVersion = apiVersion.replaceFirst(START_SLASH, "")
                    .replaceFirst(END_SLASH, "");
        }
    }

    @NonNull
    public String getBase() {
        return base;
    }

    @Nullable
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public String toString() {
        if (apiVersion != null) {
            return base + "/" + apiVersion;
        }
        return base;
    }
}
