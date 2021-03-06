# Location
Модуль для работы с местоположением.

# Описание
В модуле присутствует 2 основных класса:
* `LocationService` - сервис содержащий основные методы для работы с местоположением:
    1. проверить возможность получения местоположения;
    2. попытаться решить проблемы связанные с невозможностью получения местоположения, такие как:
        - отсутствие Runtime permission;
        - недоступность Google Play Services;
        - выключеное местоположение;
        - и другие.
    3. получить последнее известное местоположение;
    4. подписаться на обновления местоположения.

* `DefaultLocationInteractor` - интерактор для работы с местоположением, основанный на `LocationService` и содержащий методы для наиболее частых случаев использования.
    1. проверить возможность получения местоположения;
    2. попытаться решить проблемы связанные с невозможностью получения местоположения;
    3. получить последнее известное местоположение с попыткой решения проблем связанных с невозможностью получения местоположения;
    4. получить текущее местоположение с попыткой решения проблем связанных с невозможностью получения местоположения.

Более подробно в [примере](sample)

# Подключение
Gradle:
```
implementation "ru.surfstudio.android:location:X.X.X"
```