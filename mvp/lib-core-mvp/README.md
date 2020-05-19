[Главная страница репозитория](/docs/main.md)

[TOC]

# Core mvp
Создан в результате развития идей проекта [ferro](https://github.com/MaksTuev/ferro).

Расширение core-ui для гибридной архитектуры MVP + Presentation Model.

Основные особенности:

1. Презентер переживает смену конфигурации

2. Все Rx подписки приостанавливатся во время смены конфигурации,
тем самым не допускается обработка результатов [асинхронных операций][async]
пока вью пересоздается
![](https://raw.githubusercontent.com/MaksTuev/ferro/master/ferro.gif)

3. Добавлена новая сущность ScreenModel, которая является логическим
представлением ui или другими словами - полностью описывает состояние ui.
Презентер должен изменять вью только через метод ```void render(ScreenModel model)```

#### Основные сущности

Модуль содержит следующие сущности:

* [Presenter](docs/presenter.md);
* [View](docs/view.md);
* [ScreenModel](docs/screen_model.md);
* [Configurator](docs/configurator.md).


#### Подключение
Gradle:
```
    implementation "ru.surfstudio.android:core-mvp:X.X.X"
```

#### Использование

[Пример использования](../sample-core-mvp)


[async]: /docs/common/async.md