[Ридми модуля](../README.md)

[TOC]

# Конфигураторы экранов

### Что должен содержать

**ScreenConfigurator** инкапсулирует всю логику работы с [Dagger][di].
ScreenConfigurator должен содержать:

* Интерфейс наследник от ScreenComponent,
для которого следует указать View экрана как параметр типа.
Если на этом экране используются диалоги с возвращением результата,
то в этом компоненте следует также определить методы `#inject()`
для каждого из диалогов.
В компоненте должен быть указан родительский компонент
(в большинстве случаев AppComponent) в `dependencies` и
ActivityViewModule(FragmentViewModule) и другие модули,
необходимые для этого экрана в `modules`;

* Dagger модуль экрана (опционально),
который необходим передачи аргументов, с которыми стартовал экран,
и для простоты может расширять [`CustomScreenModule`][nav].

При наследовании от `BasePresenter` следует указать View экрана как параметр типа.

### Основные базовые классы
* [`BaseActivityViewConfigurator`][bavc]
* [`BaseFragmentViewConfigurator`][bfvc]
* [`ScreenComponent`][sc]
* [`ViewConfigurator`][vc]

Еще часть классов предоставляется модулем [core-ui][core-ui]

[bavc]:../src/main/java/ru/surfstudio/android/core/mvp/configurator/BaseActivityViewConfigurator.java
[bfvc]:../src/main/java/ru/surfstudio/android/core/mvp/configurator/BaseFragmentViewConfigurator.java
[sc]:../src/main/java/ru/surfstudio/android/core/mvp/configurator/ScreenComponent.java
[vc]: ../src/main/java/ru/surfstudio/android/core/mvp/configurator/ViewConfigurator.java
[di]: ../../docs/common/di.md
[core-ui]: ../../core-ui/README.md
[nav]: ../../docs/ui/navigation.md