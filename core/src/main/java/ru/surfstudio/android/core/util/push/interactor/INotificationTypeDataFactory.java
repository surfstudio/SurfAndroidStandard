package ru.surfstudio.android.core.util.push.interactor;

import java.util.Map;

/**
 * Фабрика информации о нотификации по параметру пуша
 */
public interface INotificationTypeDataFactory {
     BaseAbstractNotificationTypeData create(Map<String, String> map);
}
