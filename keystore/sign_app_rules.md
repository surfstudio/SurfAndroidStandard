
[TOC]

# Подпись приложения

Для сборки релизной версии приложения необходимо подписать apk-файл.

Для этого необходимо сделать следующее:

* создать keystore

* загрузить keystore в jenkins

* создать необходимые градл-таски

* добавить в ci по тегу
```
//configuration
pipeline.keystoreCredentials = "mdk_android_release_keystore"
pipeline.keystorePropertiesCredentials = "mdk_android_release_keystore_properties"
```

## Создание keystore

Подпись приложения будет файлом в формате *.jks.
Название файла должно соответствовать паттерну: *название_проекта_release.jks*

Для создания подписи можно воспользоваться [официальной документацией][keystore].

Также следует создать файл `keystore_release.properties`, в который поместить
следующие данные:
```
storePassword=*ключ к храниилищу*
keyPassword=*пароль к подписи*
keyAlias=*алиас*
storeFile=../keystore/*название_проекта*_release.jks
```

**Важно:**

* Не изменяйте название файла `keystore_release.properties`!

* Эти файлы необходимо поместить в директорию `keystore`.

* **Также их не стоит хранить в репозитории.**


Для того, чтобы члены команды могли локально собрать релизную версию, стоит
также расшарить на них данные файлы(например, выложить на GoogleDrive).

## Настройка проекта для работы с keystore

Далее необходимо в папке `keystore` создать файл [`keystoreConfig.gradle`][task].
Его можно скопировать к себе в проект.

В релизный `signingConfigs` приложения добавить:
```
release {
            apply from: '../keystore/keystoreConfig.gradle'

            keyAlias keystoreConfig.keyAlias
            keyPassword keystoreConfig.keyPassword
            storeFile file(keystoreConfig.storeFile)
            storePassword keystoreConfig.storePassword
        }
```

В `buildTypes` прописать `signingConfig signingConfigs.release`.

## Загрузка в Jenkins

Два файла, полученные на предыдущем этапе, `*.jks` и `*.properties` следует
загрузить в Jenkins для поддержки сборки релизной версии приложения через
**CI**(ContinuousIntegration).

todo - как выложить в дженкинс

## Настройка CI в проекте

В файл `JenkinsfileTagJob.groovy` добавить:
```
//configuration
pipeline.keystoreCredentials = "название_проекта_release_keystore"
pipeline.keystorePropertiesCredentials = "название_проекта_release_keystore_properties"

```