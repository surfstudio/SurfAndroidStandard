[Главная](../main.md)

[TOC]

# UI слой

Большинство наших приложений построено на MVP-архитектуре.

* [Структура слоя][struct]

* [Навигация][nav]

* [Работа с диалогами][dial]

* [Создание экрана][create]

#### Использование студийных модулей

Для построения ахитектуры UI слоя следует подключить в проект core-модули:
- [core-ui][core-ui]  - базовые классы ui-слоя
- [core-mvp][core-mvp]- mvp-обертка для core-ui
- [core-app](../../core-app/README.md) - стандартная конфигурация App + дополнительные сущности

Также при неодбходимости использовать диалоги с поддержкой mvp и виджеты:
- [mvp-dialog](../../mvp-dialog/README.md) - стандартные диалоги
- [mvp-widget](../../mvp-widget/README.md) - виджеты с поддержкой mvp

Опционально(**экспериметальный модуль**):
- [core-mvp-binding](../../core-mvp-binding/README.md) - модуль для биндинга

Как правильно построить UI слой можно посмотреть [здесь][core-mvp].

[core-ui]: ../../core-ui/README.md
[core-mvp]: ../../core-mvp/README.md
[struct]: structure.md
[nav]: navigation.md
[dial]: ../../mvp-dialog/README.md
[create]: create_screen.md