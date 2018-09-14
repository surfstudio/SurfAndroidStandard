Документация Android Standard
=============================

Android Standard - Репозиторий с внутренними библиотеками для android проектов студии Surf.
Содержит модули, с помощью которых можно построить качественное приложение
в короткие сроки.

[Правила ведения и оформления][rules.md] [][TODO: чтобы не забыть]

Общая структура вики
--------------------

1. **Общие сведения о построении приложения**
    1. [Требования к коду](common/code_organization.md)
    1. [Архитектура приложения](common/architect.md)
    1. *Многомодульность*
        1. [Многомодульность - общие принципы](common/multimodule/abstract.md)
        1. [Многомодульность - детали](common/multimodule/detail.md)
    1. [Инъекция зависимостей](common/di.md)
    1. [Логгирование](common/logging.md)
    1. [Шина сообщений](common/event_bus.md)
    1. [Аналитика](../analytics/README.md) [][<-- возможно сразу на модуль, так как мало информации]
    1. [Асинхронные взаимодействия](common/async.md)
    1. [Пуш-уведомления](../push/README.md)
    1. [Пагинация](common/pagin.md)

1. [**Слой Interactor**](interactor/interactor.md)
    1. [О модулях внутренней логики][interactor/about_modules_inner_logic.md]
    1. [Работа с сервером](interactor/network.md)
    1. [Работа с локацией](interactor/locatiom.md) [][<-- возможно сразу на модуль, так как мало информации]
    1. [Проверка соединения](../connection/README.md)
    1. [Миграция между версиями приложения](../app-migration/README.md)
    1. [Работа с Broadcast](../broadcast-extension/README.md)
    1. [Работа с файловым хранилищем](../filestorage/README.md)
    1. [Работа с SharedPrefs](../shared-pref/README.md)


1. [UI слой](ui/ui.md)
    1. [Структура UI-слоя](ui/structure.md)
        1. [Слой Presenter](../core-mvp/docs/presenter.md)
        1. [Слой View](../core-mvp/docs/view.md)
    1. [Создание экрана](ui/create_screen.md)
    1. [Кастомные вью](ui/custom_views.md)
    1. [Диалоги](ui/dialogs.md)
    1. [Навигация](ui/navigation.md)
    1. [Анимации](../animations/README.md)
    1. [Загрузка изображений](../imageloader/README.md)
    1. [Работа со списками](../easyadapter/README.md)
    1. [Управлениями сообщениями](../message-controller/README.md)
    1. [Загрузка данных](ui/load_data/load_data.md)
    1. [Особенности реализации View](ui/view_realization_specs.md)

1. [Инициализация приложения](../template/README.md)

1. [Лучшие практики](best_practice.md)


[][Todo]
