[Главная](../main.md)

[TOC]

# Типы сборок

[Официальная документация][docs]

В приложения принято выделять различные типы сборок.
Определить типы сборок можно в gradle-файле c помощью buildTypes {}

```
buildTypes {

        debug {
            //..
        }

        qa {
            //..
        }

        release {
            //..
        }
    }
```

Чаще всего выделяются следующие:

* debug - сборка для дебага, используется разработчиками. Эту версию
не следует обфусцировать (`minifyEnabled = false`).

* qa - сборка для тестирования. На данной сборке возможен дебаг, но включен
proguard. Это позволяет выявить ошибки из-за обфускации.

* release - сборка для релиза приложения. **Подписывается релизным ключом.**
Не предназначена для дебага.

У buildTypes существуют различные [атрибуты][attrs], с помощью которых
можно, например, управлять тем, будет ли сборка обфусцирована или нет.

Также иногда возникает необходимость расширить приведенный список.
К примеру, если есть две версии сервера - тестовая и продуктовая, то
можно завести buildTypes под каждую версию.

```
        mockApi {
            //..
            buildConfigField "String", "BASE_URL", '"https://mock.example.is"'
        }

        prodApi {
            //..
            buildConfigField "String", "BASE_URL", '"https://prod.example.is"'
        }

```
Доступ к BASE_URL осуществляется через BuildConfig.BASE_URL.

### Наследование типов сборки

Типы сборок можно наследовать. Это делается с помощью директивы initWith.
```
        debug {
            initWith buildTypes.prodApi

            buildConfigField "boolean", "IS_DEBUG", "true"
            matchingFallbacks = ['debug']
        }

        debugMock {
            initWith buildTypes.mockApi

            buildConfigField "boolean", "IS_DEBUG", "true"
            matchingFallbacks = ['debug']
        }
```

**Замечание**: initWith не поддерживает множественное наследование.
Но есть возможность наследовать еще один билд-тайп, добавив initWith
в другом файле.

Рассмотрим на примере. Есть три базовых типа сборки:

* debugBase - содержит информацию о подписи, обфускации и другие общие
атрибуты для отладочного типа.

* mockApi - содержит BASE_URL для тестового сервера

* prodApi - содержит BASE_URL для продакш сервера

Нам необходимо, чтобы debug сборка наследовалась от debugBase и использовала
продакшн сервер. Для этого выделим файл `buildTypes.gradle`, где опишем типы сборок

```

        debugBase {
            multiDexEnabled true
            minifyEnabled false
            debuggable = true
            signingConfig signingConfigs.test
            matchingFallbacks = ['debug']
        }


        /**
         * Атрибуты для сборки с тестовым сервером.
         */
        mockApi {
            matchingFallbacks = ['debug']
            buildConfigField "String", "BASE_URL", '"https://mock.example.is"'
        }

        /**
         * Атрибуты для сборки с продакшн сервером.
         */
        prodApi {
            matchingFallbacks = ['debug']
            buildConfigField "String", "BASE_URL", '"https://prod.example.is"'
        }

        debug {
            initWith buildTypes.prodApi
        }
```

Далее в `build.gradle` у корневого модуля (например app):
```
apply from: '../buildTypes.gradle'

buildTypes {

    debug {
        initWith buildTypes.debugBase
    }
}
```

Пример организации gradle-файлов можно посмотреть в [template-проекте][template]

[attrs]: http://google.github.io/android-gradle-dsl/current/com.android.build.gradle.internal.dsl.BuildType.html
[docs]: http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Build-Types
[template]: ../../template/README.md