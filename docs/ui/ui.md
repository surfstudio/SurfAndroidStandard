[Главная](../main.md)

# UI слой

- [Использование студийных модулей](#использование-студийных-модулей)

Большинство наших приложений построено на MVP-архитектуре.

* [Структура слоя][struct]

* [Навигация][nav]

* [Работа с диалогами][dial]

* [Создание экрана][create]

#### Использование студийных модулей

Для построения ахитектуры UI слоя следует подключить в проект core-модули:
- [core-ui][core_ui]  - базовые классы ui-слоя
- [core-mvp][core_mvp]- mvp-обертка для core-ui
- [core-app][core_app] - стандартная конфигурация App + дополнительные сущности

Также при неодбходимости использовать диалоги с поддержкой mvp и виджеты:
- [mvp-dialog][dial] - стандартные диалоги
- [mvp-widget][mvp_widget] - виджеты с поддержкой mvp

Опционально(**экспериметальный модуль**):
- [core-mvp-binding][mvp_binding] - модуль для биндинга

Как правильно построить UI слой можно посмотреть [здесь][core_mvp].

[core_ui]: ../../core-ui/README.md
[core_mvp]: ../../mvp/lib-core-mvp/
[core_app]: ../../deprecated/core-app/README.md
[mvp_widget]: ../../mvp/lib-mvp-widget/README.md
[mvp_binding]: ../../core-mvp-binding/README.md
[struct]: structure.md
[nav]: navigation.md
[dial]: ../../mvp/lib-mvp-dialog/README.md
[create]: create_screen.md