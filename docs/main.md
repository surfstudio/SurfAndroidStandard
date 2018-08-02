Документация Android Standard
=============================

Общая структура вики
--------------------
1. Подключение фреймворка

1. Модули

1. Архитектура проекта

1. Лучшие практики


### Подключение фреймоврка

build.gradle(root)
```groovy
allprojects {
    repositories {
        maven { url "http://artifactory.surfstudio.ru/artifactory/libs-release-local" }
    }
}
```
build.gradle(app)
```groovy
dependencies {
    implementation "ru.surfstudio.android:artifact-id:version"
}
```
Актуальные версии [здесь][https://bitbucket.org/surfstudio/android-standard/wiki/Home].

### Модули

Фреймворк содержит большое готовых модулей.
Полное описание модулей [здесь](https://bitbucket.org/surfstudio/android-standard/wiki/Modules).

#### Модули обязательные для подключения

Для быстрой инициализации проекта необходимо скопировать к себе [template-multimodule]().
А также подлючить следующие модули:

- [core-ui]()
- [core-mvp]() / возможно binding ?
- [core-app]()
- [mvp-dialog]()
- [mvp-widget]()
- [easyadapter]()
- [dagger-scope]()
- [message-controller]()
- [converter-gson]()

Остальные модули подключаются по мере необходимости.
todo

### Построение архитектуры

#### 1. Архитектура приложения
 todo взять из тех.дока
#### 2. Многомодульность
и ее взаимосвязь со слоями.
Что такое f-, i-, cf- модули, как они взаимосвязаны друг с другом.
App-injector как единая точка входа и единственный модуль с даггером.
todo
#### 3. Инъекция зависимостей
тех.док + ссылки на dagger-scope

#### 4. Шина сообщений
#### 5. Асинхронные взаимодействия
#### 6. Логгирование

### Слой Interactor
todo описать рекомендации по построению модулей, принадлежащих этому слою.

### UI слой
todo опистать шаги построения экрана (mvp / binding ?)
от чего наследоваться, в каком случае , что испольщовать. Ссылки на док  Загрузка основных данных

Кастомные вьюхи

### Лучшие практики
todo
