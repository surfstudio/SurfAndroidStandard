package ru.surfstudio.android.core.app.interactor.common.network.connection;


/**
 * Тип подключения к сети интернет.
 */
public enum InternetTypeConnection {
    UNKNOWN(""),
    WI_FI("Wi-Fi"),
    MOBILE("Mobile");

    private String type;

    InternetTypeConnection(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}