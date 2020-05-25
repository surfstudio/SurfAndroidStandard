[TOC]

# Permission
Является расширением [`core-ui`][core-ui]-модуля для работы с Runtime Permissions (разрешениями приложения).

# Механизм работы:

1. Создать класс-наследник от [`PermissionRequest`][permreq], в котором будут описаны необходимые для получени разрешения. Дополнительно в нем может содержаться:

    * Список необходимых разрешений;
    * Флаг, определяющий, нужно ли показать поясняющий диалог перед запросом разрешения;
    * Текст поясняющего диалога;
    * Route поясняющего диалога;
    * Флаг, определяющий, нужно ли перейти в настройки, если мы уже однажды полностью запретили доступ к необходимым разрешениям;
    * Текст поясняющего диалога для перехода в настройки;
    * Route поясняющего диалога для перехода в настройки

1. Передать этот новосозданный класс в [`PermissionManager`][permman]`.request()` и подписаться на результат работы этого метода.

1. В случае успешного получения разрешений, результат будет true, иначе - false.

# Использование
[Пример использования](../sample)

### Подключение

Gradle:
```
    implementation "ru.surfstudio.android:permission:X.X.X"
```

[core-ui]: ../../core-ui/lib-core-ui/README.md
[permreq]: src/main/java/ru/surfstudio/android/core/ui/permission/PermissionRequest.kt
[permman]: src/main/java/ru/surfstudio/android/core/ui/permission/PermissionManager.kt