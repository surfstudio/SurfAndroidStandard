#Picture provider
Содержат в себе набор классов для а получения изображений с устройства.

#Использование
[Sample проект](../picture-provider-sample/README.md)
##Основные классы
`PicturePermissionChecker` логика проверки и получения разрешений для работы с изображениями
`PictureProvider` логика получения изображений с устройства. Работает через rx.

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:picture-provider:X.X.X"
```