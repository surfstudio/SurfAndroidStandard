# Многомодульное приложение

В студии используется многомодульная архитектура проектов.
В этом модуле содержится минимальная конфигурация приложения.

[Пара слов об InstantApp][instant]

## Многомодульность

Многомодульное приложение обладает следующими преимуществами:
 - скорость сборки
 - паралельная сборка модулей
 - изоляция слоев

В студийной архитектуре выделяем следующие модули:
 - [app-injector](#app-injector) - основной модуль приложения(точка входа). Знает обо всех модулях и
   является **единственным модулем с даггер зависимостями**. От него не зависит ни один другой модуль,
   он же зависит от всех.
 - [domain](#domain) - слой **Domain**. Содержит все ,что касается
 доменной области (модели данных).
 - [base](#base) - базовый модуль. Здесь объявляются зависимости необходимые во всех других модулях
   (через api). Также содержит базовые сущности , использующиеся как в i-, так в f-модулях.
 - [base-ui](#base-ui) - базовый слой для модулей фич и вообще пользовательских представлений.
   Зависит от base.

 - [f-*name*](#f-модули) - feature-модули. Модули, в которых ведется разработка конкретной фичи.
   Это может быть несколько экранов с одной семантикой (например авторизация), может быть один большой экран со сложной логикой.
   **Не зависят друг от друга!!!**
 - [cf-*name*](#cf-модули) - common-feature-модули. Модули c фичами,
 которые переиспользуются в других фичах. **Не зависят друг от друга!!!**
 - [i-*name*](#i-модули) - модули-интеракторы. Данные модули принадлежат слою **Interactor**.
   Могут выполнять конкретный UseCase, могут содержать несколько интеракторов.
   **Могут зависеть друг от друга.**


При таком разбиении изменение одного фича-модуля влекут процесс сборки
только в нем и в app-injector.

## App-injector

Данный модуль является корневым модулем проекта. Все остальные модули являются библиотечными.
Особенность данного модуля - знать обо всех других модулях и иметь даггер зависимости.
(Только в этом модуле подключается *annotatiоnProccesor* из-за этого становиться возможной
инкрементальная сборка других модулей -> ускорение сборки).

Модуль может содержать пакеты , принадлежащие слоям и interactor и ui.
Также содержит сам класс приложения App (наследуется от
[CoreApp](../../../core-app/src/main/java/ru/surfstudio/android/core/app/CoreApp.java) или его наследников).

При инициализации приложения необходимо сконфигурировать Dagger-модули и компоненты.
Пример конфигурации можно посмотреть в шаблоне.

**!!! Важно !!!**

1. Для того , чтобы конфигураторы экранов были доступны в модулях фич создается object
ScreenConfiguratorStorage.
``` kotlin

object ScreenConfiguratorStorage {

    val activityScreenConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> ActivityScreenConfigurator>()
            .apply {
                put(MainActivityView::class, { MainScreenConfigurator(it) })
            }

    val activityConfiguratorMap = HashMap<KClass<*>, (intent: Intent) -> ActivityConfigurator>()
            .apply {
            }

    val fragmentScreenConfiguratorMap = HashMap<KClass<*>, (args: Bundle) -> FragmentScreenConfigurator>()
            .apply {

            }

    val dialogScreenConfiguratorMap= HashMap<KClass<*>, (args: Bundle) -> DialogScreenConfigurator>()
            .apply {

            }

    val widgetScreenConfiguratorMap = HashMap<KClass<*>, () -> WidgetScreenConfigurator>()
            .apply {
            }
}

```


2. Для того, чтобы производить навигацию между модулями, необходимо создать
 object RouteConfiguratorStorage

``` kotlin
object RouteConfigurationStorage {

    val fragmentRouteConfiguratorMap = HashMap<KClass<*>,  KClass<*>>()
            .apply {
                put(CommentsFragmentRoute::class, CommentsFragmentView::class )
            }

    val dialogRouteConfiguratorMap = HashMap<KClass<*>,  KClass<*>>()
            .apply {
                put(BalanceStatsDialogRoute::class, BalanceStatsDialogView::class.java )
                put(ClaimDialogRoute::class, ClaimDialogView::class.java )
            }
}
```

## domain

Модуль самого нижнего уровня - от него зависят все остальные модули.
Содержит чаще всего модели данных.
Полностью соответсвует слою Domain.

## Base

Модуль , содержащий базовые сущности необходимые для работы i-модулей:
* CallAdapterFactory
* типы ошибок

Зависит от модуля domain.
В данном модуле происходит подключение библиотек необходимых по всему приложению,
так как именно от него зависят остальные модули.
Подключение зависимостей происходит через api.
``` groovy
api "ru.surfstudio.android:animations:$surfCoreVersionName"
```

api - передает зависимости в дальнейшие модули.

Конфигурация модулей AndroidStandard происходиьт также здесь.

Модули рекомендуемые к подключению: [здесь](https://bitbucket.org/surfstudio/android-standard/wiki/Modules)

## base-ui

Базовый модуль ui слоя. Содержит переиспользуемые виджеты, контроллеры, и тд.
Содержит базовые обработчики ошибок.

**!! Важно!! **
Содержит ComponentProvider - сущность , которая будет поставлять на экраны
зависимости из app-injector, например, конфигураторы экранов или классы для навигации.
Инициализация его свойств происходит в App.

``` kotlin
 private fun initComponentProvider() {
        ComponentProvider.createActivityScreenConfigurator = { intent, kclass ->
            ScreenConfiguratorStorage.activityScreenConfiguratorMap[kclass]?.invoke(intent)!!
        }

        ComponentProvider.createActivityConfigurator = { intent, kclass ->
            ScreenConfiguratorStorage.activityConfiguratorMap[kclass]?.invoke(intent)!!
        }

        ComponentProvider.createFragmentScreenConfigurator = { bundle, kclass ->
            ScreenConfiguratorStorage.fragmentScreenConfiguratorMap[kclass]?.invoke(bundle)!!
        }

        ComponentProvider.createDialogScreenConfigurator = { bundle, kclass ->
            ScreenConfiguratorStorage.dialogScreenConfiguratorMap[kclass]?.invoke(bundle)!!
        }

        ComponentProvider.createWidgetScreenConfigurator = { kclass ->
            ScreenConfiguratorStorage.widgetScreenConfiguratorMap[kclass]?.invoke()!!
        }
    }
```


## i-модули

Эти модули зависят от base-модуля. В идеале не содержат зависимостей Android Framework.
Но иногда могут выделяться интеракторы под цели общения с системой (например,
сохранение данных в Shared Prefs).

Также эти модули могут иметь иерхихию: например, для всех i-модулей ,
работающих с сетью, выделяется общий базовый i-network.

## f-модули

Фича-модули(feature) предназначены для разработки конкретных фич.
Это может быть как один экран, так и несколько экранов с общей задачей, например
экраны авторизации.

Базовой зависимостью для таких модулей слжит модуль **base-ui**.

От этих модулей модет зависеть только app-injector. Это обеспечивает то, что
изменения в них не будут вызывать пересборку других, независимых модулей.

## cf-модули

Это те же f-модули, но с другим наименованием и содержащие переиспользуемые фичи.

Индикатором того, что модуль можно пометить как *common-feature*, может служить
то, что содержание модуля не является законченной "фичей" (цельным экраном приложения,
стеком экранов с общей целью), но и выходит за рамки простой кастомной View.
Как пример -  некоторый фрагмент, используемый на нескольких экранах приложения.

Такой подход помогает увидеть переиспользуемые компоненты сразу из дерева.

**Все правила , относящиеся к f-модулям, справедливы и для cf.**


[instant]: instant.md

