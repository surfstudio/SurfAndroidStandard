# Пример создания собственного Scope

- [Добавление кастомного скоупа между `@PerApplication` и `@PerActivity`](#добавление-кастомного-скоупа-между-perapplication-и-peractivity)
- [Добавление сущности в ActivityScope конкретной Activity](#добавление-сущности-в-activityscope-конкретной-activity)

Используются студийные [Scope](../lib-dagger-scope/README.md).
Общая информация об использовании [здесь](/docs/common/di.md).

В данном примере некоторый объект [`EmailData`][ld] живет дольше, чем две активити,
но меньше чем приложение. Например, это могут быть данные , который заполнил пользователь.
В случае если он покинул экран по беку, а потом вернулся, необходимо, чтобы
эти данные были предзаполнены.
Управление временем жизни компонента выполняется "в ручную", c помощью
[`LoginScopeStorage`][lss].

## Добавление кастомного скоупа между `@PerApplication` и `@PerActivity`

Иногда возникает ситуация, когда надо сделать выделенную область видимости
для нескольких активити.

Для этого необходимо:

- создать аннотацию , например [`@PerLogin`][pl]

- создать Dagger-компонент, ex. LoginComponent, который будет наследником
AppComponent.

- создать статичное хранилище(LoginScopeStorage), которое будет ответсвенным за время жизни
данного компонента. То есть будет уничтожать компонент, когда нет ни одной
активити принадлежащей скоупу.

- в конфигураторе активити [`ActivityScreenConfigurator`][asc] сменить тип компонента-родителя
на [`LoginComponent`][lcomp] -> в методе getParentComponent() получать компонент через
хранилище. Либо создать отдельный конфигуратор для этого скоупа.
([`LoginActivityScreenConfigurator`][lasc]).

- не забыть прокинуть EmailData в ActivityComponent, либо в другой компонент
(смотри ниже)

## Добавление сущности в ActivityScope конкретной Activity

* Создать кастомный компонент `LoginActivityComponent`, который будет иметь скоуп
`@PerActivity`, зависеть от `AppComponent`(Либо от другого вышестояшего скоупа,
например `LoginComponent` как в сэмпле), и наследоваться от `ActivityComponent`,
чтобы не дублировать зависимости.

* Создать модуль с необходимыми зависимостями.

* Добавить этот модуль к компоненту, а так же прокинуть через него зависимости
из `AppComponent`

* Создать [`LoginActivityScreenConfigurator`][lasc], который будет абтрактным и
станет базовым для будущего экрана. Его код будет аналогичен `ActivityScreenConfigurator`
За исключением того, что он будет типизирован `LoginActivityComponent`'ом


[ld]: src/main/java/ru/surfstudio/android/custom_scope_sample/domain/EmailData.kt
[lss]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/LoginScopeStorage.kt
[pl]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/dagger/scope/PerLogin.kt
[asc]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/configurator/ActivityScreenConfigurator.java
[lcomp]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/dagger/login/LoginComponent.kt
[lasc]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/configurator/LoginActivityScreenConfigurator.java