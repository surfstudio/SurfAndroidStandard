# Пример создания собственного Scope

Используются студийные [Scope][../dagger-scope/README.md].
Общая информация об использовании [здесь][../docs/common/di.md].

В данном примере некоторый объект [`EmailData`][ld]живет дольше чем две активити,
но меньше чем приложение. Например, это могут быть данные , который заполнил пользователь.
В случае если он покинул экран по беку, а потом вернулся, необходимо, чтобы
эти данные были предзаполнены.
Управление временем жизни компонента выполняется "в ручную", c помощью
[`LoginScopeStorage`][lss].

# Добавление кастомного скоупа между `@PerApplication` и `@PerActivity`

Иногда возникает ситуация, когда надо сделать выделенную область видимости
для нескольких активити.

Для этого необходимо:
- создать аннотацию , например [`@PerLogin`][pl]
- создать Dagger-компонент, ex. LoginScreenComponent, который будет наследником
AppComponent.
- создать статичное хранилище(LoginScopeStorage), которое будет ответсвенным за время жизни
данного компонента. То есть будет уничтожать компонент, когда нет ни одной
активити принадлежащей скоупу.
- в конфигураторе активити [`ActivityScreenConfigurator`][asc] сменить тип компонента-родителя
на [`LoginScreenComponent`][lcomp] -> в методе getParentComponent() получать компонент через
хранилище.

- не забыть прокинуть EmailData в ActivityComponent

[ld]: src/main/java/ru/surfstudio/android/custom_scope_sample/domain/EmailData.kt
[lss]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/LoginScopeStorage.kt
[pl]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/dagger/scope/PerLogin.kt
[asc]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/configurator/ActivityScreenConfigurator.java
[lcomp]: src/main/java/ru/surfstudio/android/custom_scope_sample/ui/base/dagger/login/LoginScreenComponent.kt