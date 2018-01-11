package ru.surfstudio.android.core.util.push.ui;

import java.util.HashMap;
import java.util.Map;


/**
 * Данные пуш уведомления
 */

public class NotificationData {
    private String title;
    private String body;
    private Map<String, String> data = new HashMap<>();

    public NotificationData(String title, String body, Map<String, String> data) {
        this.title = title;
        this.body = body;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getData() {
        return data;
    }
}
