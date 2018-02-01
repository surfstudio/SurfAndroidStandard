package ru.surfstudio.android.core.util.push.ui;


import android.app.Activity;

import ru.surfstudio.android.core.util.push.interactor.BasePushInteractor;
import ru.surfstudio.android.logger.RemoteLogger;

/**
 * Выполняем необходимые действия при пуше на ui
 * Определяет нужно ли послать событие в пуш интерактор, либо же
 * создать нотификацию открытия экрана по пушу
 */
public abstract class BasePushHandler<T extends BasePushInteractor> {

    protected abstract Activity getActivity();

    protected abstract T getPushInteractor();

    protected abstract PushHandleStrategy<T> createPushHandleStrategy(NotificationData data);

    public void handleMessage(NotificationData data) {
        Activity activity = getActivity();
        if (activity != null) {
            createPushHandleStrategy(data).handle(activity, getPushInteractor(), data);
        } else {
            RemoteLogger.logMessage("Cant handle push body no active activity " + data); //todo обычный логгер
        }
    }
}
