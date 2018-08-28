#Механизм локальной загрузки модулей AndroidStandard

Данный механизм может быть использован для отладки и экспериментов.

Если необходимо изменить часть функционала android-standard и протестировать на своем проекте
без деплоя самого android-standard, то можно выполнить необходимые изменения в локальном репозитории
android-standard и подключить его к своему проекту.

##Настройка

+ Скопировать данную папку android-standard с необходимыми скриптами в корень проекта;
+ Добавить в папку android-standard файл ```androidStandard.properties``` со следующим содержимым:
```
androidStandardDebugDir=/full/path/to/your/local/android-standard
androidStandardDebugMode=true       # флаг для активации режима локальной загрузки репозитория android-standard
```
+ Добавить файл ```androidStandard.properties``` в ```gitignore``` проекта следующим образом: ```/android-standard/androidStandard.properties```.

##Использование

+ **settings.gradle**

В данный файл необходимо первой строкой добавить ```apply from: 'android-standard/androidStandardConfig.gradle'```
для выполнения инициализации объекта ```gradle.ext.androidStandard```.

Затем необходимо подключить все модули проекта: ```include ':first-module', ':second-module'```.

В конец данного файла необходимо добавить строку ```apply from: 'android-standard/androidStandardSettings.gradle'```
для подключения локального репозитория android-standard.

+ **build.gradle** уровня модуля приложения

В конец данного файла необходимо добавить строку ```apply from: '../android-standard/androidStandardDependencies.gradle'```
для подключения модулей android-standard локально или из artifactory.

##Замечения
После каждого изменения параметра ```androidStandardDebugMode``` для успешной сборки проекта
необходимо выполнить ```Build - Clean Project```.