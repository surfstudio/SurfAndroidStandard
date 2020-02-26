Стандарты разработки приложений в Surf
=============================

[Правила ведения и оформления](rules.md)

Общая структура вики
--------------------

1. **Общие сведения о построении приложения**
    1. [Требования к коду](common/code_organization.md)
        1. [Java Code Style](common/codestyle/java_codestyle.md)
        1. [Kotlin Code Syle](common/codestyle/kotlin_codestyle.md)
    1. [Архитектура приложения](common/architect.md)
    1. [Многомодульность](common/multimodule/multimodules.md)
    1. [Инъекция зависимостей](common/di.md)
    1. [Логгирование](common/logging.md)
    1. [Шина сообщений](common/event_bus.md)
    1. [Асинхронные взаимодействия](common/async.md)
    1. [Пагинация](common/pagin.md)
    1. [Аналитика](../analytics/sample/README.md)
    1. [Пуш-уведомления](../push/lib-push/README.md)

1. [**Слой Interactor**](interactor/interactor.md)
    1. [Рассылка событий через интерактор](interactor/events_by_interactor.md)
    1. [Работа с сервером](interactor/network.md)
        1. [Кеширование](interactor/cache.md)
        1. [Гибридные запросы](../deprecated/network/lib-network/docs/hybrid.md)
    1. [Работа с локацией](../location/sample/README.md)
    1. [Проверка соединения](../connection/lib-connection/README.md)
    1. [Миграция между версиями приложения](../app-migration/sample/README.md)
    1. [Работа с Broadcast](../broadcast-extension/sample/README.md)
    1. [Работа с файловым хранилищем](../filestorage/sample/README.md)
    1. [Работа с SharedPrefs](../shared-pref/sample/README.md)

1. [UI слой](ui/ui.md)
    1. [Структура UI-слоя](ui/structure.md)
        1. [Presenter](../mvp/lib-core-mvp/docs/presenter.md)
        1. [View](../mvp/lib-core-mvp/docs/view.md)
        1. [ScreenModel](../mvp/lib-core-mvp/docs/screen_model.md)
    1. [Навигация](ui/navigation.md)
    1. [Создание экрана](ui/create_screen.md)
    1. [Диалоги](../mvp/lib-mvp-dialog/README.md)
    1. [Кастомные вью](ui/custom_views.md)
    1. [Виджеты](../mvp/lib-mvp-widget/README.md)
    1. [Анимации](../animations/sample/README.md)
    1. [Загрузка изображений](../imageloader/sample/README.md)
    1. [Работа со списками](../easyadapter/sample/README.md)
    1. [Управлениями сообщениями](../message-controller/sample/README.md)
    1. [Загрузка данных](ui/load_data/load_data.md)
    1. [Особенности реализации View](ui/view_realization_specs.md)

1. [Инициализация приложения](../template/README.md)
   1. [DEPRECATED Подготовка проекта к выгрузке в fabric](common/fabric.md)
   2. [Миграция приложения с fabric на firebase app distribution](common/firebase/app_distribution_migration.md)
   3. [Миграция приложения с fabric crashlytics на firebase crashlytics](common/firebase/firebase_crashlytics_migration.md)

1. [Безопасность](../security/sample/README.md)

1. [Лучшие практики](best_practice/best_practice.md)
    1. [Поиск утечек](best_practice/memory_leak.md)
    2. [CI](https://github.com/surfstudio/jenkins-pipeline-lib)
    3. [Настройки и плагины для Android Studio](../android-studio-settings/android_studio_settings.md)