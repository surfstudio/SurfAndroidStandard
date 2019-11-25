# Скрипты с механизмом подключения модулей Android Standard

Скрипты позволяют переключаться между локальным искодным кодом android-standard и артефактами из artifactory.
Использование локального исходного кода позволит быстро тестировать изменения в android-standard на своем проекте без деплоя артефактов.

## Первичная настройка на конкретной машине

1. Добавить в папку android-standard файл ```androidStandard.properties``` со следующим содержимым:
```
androidStandardDebugDir=/full/path/to/your/local/android-standard
# флаг для активации режима локальной загрузки репозитория android-standard
androidStandardDebugMode=true
```

Режим локальной сборки активируется только при наличии пути в соответствующей переменной и выставленном флаге

2. Выполнить ```File - Sync Project with Gradle Files```.

## Переключение источника android-standard

1. Изменить флаг ```androidStandardDebugMode``` в файле ```android-standard/androidStandard.properties```
2. Выполнить ```File - Sync Project with Gradle Files```.
3. При переключении в режим локальной сборки и недоступности artifactory с android-standard-version-plugin убрать строки:
из секции dependencies корневого build.gradle ```classpath "ru.surfstudio.android:version-plugin:$androidStandardVersion"```
и ```apply plugin: 'ru.surfstudio.android'``` из всех build.gradle

## Подключение скриптов к сборщику gradle
Этот раздел будет полезен для тех кто собирается перенести эти скрипты в существующий проект

+ **settings.gradle** уровня проекта

В конец данного файла необходимо добавить

```apply from: 'android-standard/androidStandardSettings.gradle'```

для подключения локального репозитория android-standard.

+ **build.gradle** уровня модуля приложения

Добавить модули android-standard следующим образом:

```
dependencies {
    // other dependencies

    gradle.ext.androidStandard.api(
                this,
                [
                    "core-ui",     // массив модулей android-standard, который необходимо подключить к текущему модулю
                    "core-mvp",
                    "core-app"
                ]
    )
}
```

**Важно:** все модули android-standard следует подключать только таким образом, не использовать

  ```implementation "ru.surfstudio.android:module-name:${version}"```

+ **gitignore** уровня проекта

Добавить ```/android-standard/androidStandard.properties```

+ **buildTypes**

Если проект содержит кастомные ```buildTypes```, отличные от ```debug``` и ```release```, необходимо
предоставить для них ```matchingFallbacks``` следующим образом:

```
buildTypes {
        qa {
            // для buildType.qa необходимо сопоставить buildType.release
            matchingFallbacks = ['release']
        }
        customBuildType {
            matchingFallbacks = ['debug']
        }
}
```

Это необходимо сделать для успешной сборки проекта. Так как android-standard содержит 2 buildTypes,
при его подключении необходимо сопоставить кастомные buildTypes к тем, какие определены в android-standard.