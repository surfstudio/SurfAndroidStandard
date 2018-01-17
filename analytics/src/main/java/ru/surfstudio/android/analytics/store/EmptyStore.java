package ru.surfstudio.android.analytics.store;


import android.support.annotation.NonNull;

import java.util.Map;

import ru.surfstudio.android.analytics.Analytics;

/**
 * Делает ничего.
 * При использовании этого класса события не отправляются ни в какой сервис аналитики
 */
public class EmptyStore implements Analytics {

    @Override
    public void sendEvent(String event) {
        //nothing
    }

    @Override
    public void sendEvent(String event, @NonNull Map<String, String> params) {
        //nothing
    }

    @Override
    public void setUserProperty(String key, String value) {
        //nothing
    }
}

