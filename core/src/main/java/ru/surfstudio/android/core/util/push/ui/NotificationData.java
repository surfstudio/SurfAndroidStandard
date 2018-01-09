package ru.surfstudio.android.core.util.push.ui;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Данные пуш уведомления
 */
@Data
@AllArgsConstructor
public class NotificationData {
    private String title;
    private String body;
    private Map<String, String> data = new HashMap<>();
}
