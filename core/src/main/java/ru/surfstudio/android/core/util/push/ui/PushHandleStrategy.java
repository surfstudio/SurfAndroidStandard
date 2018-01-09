package ru.surfstudio.android.core.util.push.ui;

import android.app.Activity;

import ru.surfstudio.android.core.util.push.interactor.BasePushInteractor;

/**
 * Стратегия обработки пуша если приложение запущено
 */
public interface PushHandleStrategy<T extends BasePushInteractor> {
    /**
     * Требуемое действие нотификации
     *
     * @param activity       текущая активная активити
     * @param pushInteractor пуш интерактор
     * @param data           данные пуша
     */
    void handle(Activity activity, T pushInteractor, NotificationData data);
}
