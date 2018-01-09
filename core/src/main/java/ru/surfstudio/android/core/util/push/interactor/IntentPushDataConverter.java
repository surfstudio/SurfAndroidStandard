package ru.surfstudio.android.core.util.push.interactor;

import android.content.Intent;

import com.annimon.stream.Stream;

import java.util.HashMap;
import java.util.Map;

/**
 * Конвертирует данные пуша из интента в мапу
 */
public class IntentPushDataConverter {

    private IntentPushDataConverter() {
        //do nothing
    }

    public static Map<String, String> convert(Intent intent) {
        if (intent.getExtras() == null) {
            return new HashMap<>();
        }
        Map<String, String> data = new HashMap<>(intent.getExtras().size());
        Stream.of(intent.getExtras().keySet()).forEach(
                value ->
                        data.put(value, intent.getExtras().get(value).toString()));
        return data;
    }
}
