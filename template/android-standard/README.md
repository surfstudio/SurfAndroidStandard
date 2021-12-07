#Скрипты с механизмом подключения модулей Android Standard

Скрипты позволяют переключаться между локальным искодным кодом android-standard и артефактами из artifactory.
Использование локального исходного кода позволит быстро тестировать изменения в android-standard на своем проекте без деплоя артефактов.

##Термины

**Локальный Android Standard** - локальная копия Android Standard, клонированная из удаленного
репозитория. В том случае, если ваш проект использует проектный снапшот Android Standard, следует
переключить его на соответствующую ветку (`project-snapshot/PROJECT-TAG`)
**Рабочий проект** - исходный код приложения, для которого выполняется конфигурация для работы с локальным Android Standard

#Важно!
Для корректной работы локального подключения необходимо, чтобы версии gradle в проекте и в ветке AS совпадали!

##Первичная настройка на конкретной машине

1. В рабочем проекте добавить в папку android-standard файл `androidStandard.properties` со следующим содержимым:
```
androidStandardDebugDir=/full/path/to/your/local/android-standard
# флаг для активации режима локальной загрузки репозитория android-standard
androidStandardDebugMode=false
# флаг для отключения сборки примеров к модулям при локальном подключении
skipSamplesBuild=true
```

2. В рабочем проекте выполнить ```File - Sync Project with Gradle Files```.

##Переключение источника android-standard

1. В рабочем проекте изменить флаг `androidStandardDebugMode` в файле `android-standard/androidStandard.properties`
2. В рабочем проекте выполнить `File - Sync Project with Gradle Files`.

**ВАЖНО!** При активном локальном подключении при любом изменении флага
`skipSamplesBuild` делать Gradle Sync перед сборкой проекта.

##Подключение скриптов к сборщику gradle
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