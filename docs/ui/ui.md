# UI слой

Ядром UI-слоя является модуль [core-ui][core-ui].

Большинство наших приложений построено на MVP-архитектуре.
Слои View и Presenter принадлежат к UI. Для реализации MVP используется
надстройка над core-ui в виде [core-mvp][core-mvp].


#### Иcпользование студийных модулей
Для построения ахитектуры UI слоя следует подключить в проект core-модули:
- [core-ui](../core-ui/README.md)  - базовые классы ui-слоя
- [core-mvp](../core-mvp/README.md)- mvp-обертка для core-ui
- [core-app](../core-app/README.md) - стандартная конфигурация App + дополнительные сущности

Также при неодбходимости использовать диалоги с поддержкой mvp и виджеты:
- [mvp-dialog](../mvp-dialog/README.md) - стандартные диалоги
- [mvp-widget](../mvp-widget/README.md) - виджеты с поддержкой mvp

Опционально(**экспериметальный модуль**):
- [core-mvp-binding](../core-mvp-binding/README.md) - модуль для биндинга

Как правильно построить UI слой можно посмотреть [здесь](../core-mvp/README.md).

#### Кастомные вью

Для создания кастомных вью рекомендуется следовать рекомендациям описанным в
[здесь](https://docs.google.com/document/d/1Scu3QXgpVLNpTLOP6nwTnBNWXRQueMiHukfAip_ZLP0/edit#heading=h.ah2nz5eiite7).

Создание виджетов с презентером -> [здесь](../mvp-widget/README.md)




