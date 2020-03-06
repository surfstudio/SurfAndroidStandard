[Главная](../main.md)

[TOC]

# Создание нового экрана

##### Создание экрана
Для создания экрана необходимо проделать следующие шаги:

1. Расширить [4 класса ..Route, BasePresenter, ..View, , ..ScreenModel][core-mvp]

1. Расширить [`ScreenConfigurator`][configurator] в app-injector'е и занести его в
[ScreenConfiguratorStorage][multi];

1. Получить ScreenConfigurator через [`ComponentProvider`][multi] на экране.

##### Шаблоны

Для создания нового экрана предусмотрены шаблоны.
Чтобы их использовать необходимо выполнить следующее:

1. Скопировать папку `surf` из директории `android-standard/android-studio-settings/file`
в папку `<android-studio-folder>/plugins/android/lib/templates`

1. Нажать ПКМ в дереве пакетов -> New -> Surf -> Activity/Fragment

Шаблоны работают в студии 3.0 и выше

[core-mvp]: ../../mvp/lib-core-mvp/README.md
[multi]: ../common/multimodule/detail.md
[configurator]: ../../mvp/lib-core-mvp/docs/configurator.md