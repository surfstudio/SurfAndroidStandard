package ru.surfstudio.android.core.util.push.interactor;

import java.io.Serializable;

/**
 * ключ определяющий тип нотификации
 */
public interface NotificationTypeKey extends Serializable {
    String getKey();
}
