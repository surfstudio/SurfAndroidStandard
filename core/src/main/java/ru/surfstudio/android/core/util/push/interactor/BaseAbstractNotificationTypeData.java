package ru.surfstudio.android.core.util.push.interactor;

import java.io.Serializable;
import java.util.Map;

/**
 * Тип пуш уведомления с данными
 * <p>
 * NotificationTypeKey задет тип пуша с сервера
 * Конкретный наследний этого класса используется для определения необходимого действия
 * Используется для передачи и подписки на события в PushInteractor и для
 * необходимых действий при холодном старте приложения
 */
public abstract class BaseAbstractNotificationTypeData<T extends Serializable, A extends BasePresenterActions> implements Serializable {
    public static final String EXTRA_KEY = "extra_notification_key";

    private final NotificationTypeKey type;
    private T data;

    public BaseAbstractNotificationTypeData(Map<String, String> map) {
        type = defineNotificationType();
        setData(map);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public NotificationTypeKey getType() {
        return type;
    }

    /**
     * Действие клика на сообщение пуша при холодном старте приложения
     */
    public abstract void performAction(A actions);

    /**
     * @return тип уведомления соотвествующий данным
     */
    public abstract NotificationTypeKey defineNotificationType();

    /**
     * @return извлекает данные из параметров
     */
    public abstract T extractData(Map<String, String> params);

    /**
     * установка данных из параметров
     */
    protected void setData(Map<String, String> params) {
        setData(extractData(params));
    }
}
