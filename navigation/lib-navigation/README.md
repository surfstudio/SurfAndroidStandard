[Главная страница репозитория](/docs/main.md)

# Navigation

## Предпосылки появления

Изначально созданная навигация через навигаторы-обертки показала себя не с лучшей стороны
в сложных сценариях использования: она хорошо решала проблемы типового приложения с разделением на
фичи-activity, однако при попытках расширить и кастомизировать этот подход, разработчики
сталкивались с необходимостью полной переработки навигаторов на собственных проектах.

## Проблемы, которые решает модуль navigation.

1. Нет единообразия при работе с навигацией для Activity, Fragment и Dialog.
Запуск этих экранов выглядит совершенно по-разному и в android framework, и в предыдущих разработках Surf.
Модуль navigation решает эту проблему и приводит всю работу с экранами к единой системе: навигации на основе команд.
1. Сложность при организации Single-Activity приложений.
В предыдущей реализации навигации для Fragments,
у разработчиков была возможность опуститься только на один уровень вглубь стека фрагментов
(можно было использовать FragmentNavigator для операций с supportFragmentManager,
и ChildFragmentNavigator для операций с childFragmentManager).
Операции с табами (TabFragmentNavigator) поддерживались только на уровне supportFragmentManager.
Эта проблема решается в модуле navigation гибким запуском команд навигации:
добавлена возможность углубиться на любой уровень вложенности фрагментов, и переключаться между этими уровнями.
1. Асинхронность навигации.
Все команды этого модуля выполняются последовательно и условно-синхронно, что позволяет вызывать цепочки команд
не опасаясь за то, что предыдущий экран еще не будет готов.
1. Излишнее количество реализаций Route. В старом подходе навигации у Route огромное количество
реализаций, многие из которых просто не нужны, и заставляют пользователя путаться при выборе.
В текущей реализации базовых Route есть всего несколько, и они разделены по типам экранов:
[ActivityRoute][aroute], [FragmentRoute][froute], [Dialog][droute] и [Widget][wroute].

## Общий принцип работы.

Принцип работы модуля заключается в следующем:

1. У каждого экрана есть точка входа: [Route][route]. Она содержит все входные данные, необходимые для запуска экрана,
однозначно идентифицирует экран на основе этих параметров. Базовая реализация ([BaseRoute][baseroute])
является базовым интерфейсом:
    * getScreenClass, getScreenClassPath - извлечение класса экрана, по которому будет происходит запуск экрана.
    * getTag - идентификация экрана
    * prepareData - предоставление стартовых данных для открытия экрана.
1. Для каждого типа экрана существуют навигаторы ([ActivityNavigator][anav], [FragmentNavigator][fnav], [DialogNavigator][dnav]),
позволяющие выполнить открытие, закрытие и другие действия с экраном, основываясь на route.
Навигаторы живут в скоупе экрана, умирают и пересоздаются вместе с ним.
Если навигаторы содержат бекстек, он сохраняется и восстанавливается при смене конфигурации.
1. Любое действие, производимое над экраном, заключается в класс [NavigationCommand][navcom]. Команда содержит в себе [Route][route],
возможные анимации [Animations][anim], и другие опции для открытия экрана.
1. Для того, чтобы направлять команды в нужные навигаторы, существуют [CommandExecutor][exec]-сущности.
У каждого типа экрана есть свой Executor, и он ограничен пулом команд экрана, которые в него поступают.
Все Executor'ы - это синглтоны, и живут столько же, сколько живет приложение.
1. Все Executor'ы объединяются в один, общий [AppCommandExecutor][appexec],
который распределяет команды между экранными и обеспечивает последовательное выполнение.


## Сохранение состояния и dependency injection

Данный модуль не привязан к стандартным скоупам core-ui, и не имеет зависимостей от DI-фреймворков.
Сохранение состояния и привязка переменных к определенным экранам здесь реализована
через `ActivityLifecycleCallbacks` и `FragmentLifecycleCallbacks`.

Все необходимые для Activity-навигации классы создаются в провайдерах: [ActivityNavigationProvider][aprov],
и его реализации ActivityNavigationProviderCallbacks[aprovcal].
Здесь для каждой Activity в onCreate создаются [ActivityNavigator][anav], [DialogNavigator][dnav], а также меняется активный в данный момент навигатор в зависимости от того,
какая Activity в данный момент находится в состоянии Resumed.

Все необходимые для `Fragment`-навигации классы создаются в провайдерах: FragmentNavigationProvider[fprov],
и его реализации FragmentNavigationProviderCallbacks[fprovcal].
Здесь для `Fragment` в onFragmentCreated создаются [FragmentNavigator][fnav] и TabFragmentNavigator[tfnav],
а также происходит переключение между навигаторами разных фрагментов:
извлечение нужного происходит по идентификатору (тегу) фрагмента, в котором навигатор был создан.

**Внимание!** Fragment будет содержать навигаторы, только если он наследуется от [FragmentNavigationContainer][fnavcontainer],
то есть, в нем есть ViewGroup, внутри которого будут помещены дочерние фрагменты.

Извлечение нужного навигатора производится через `FragmentNavigationProvider.provide(sourceTag)`.
`sourceTag` здесь - это тег фрагмента, который содержит навигацию.
В механизме навигации через CommandExecutor, этот тег предоставляется через `FragmentCommand.sourceTag`.
Если необходимо явно вызвать навигатор из Activity (SupportFragmentManager), в качестве SourceTag может быть передан
ACTIVITY_NAVIGATION_TAG.

Для того, чтобы постоянно не указывать sourceTag, существует `ScreenScopeCommandExecutor`,
который действует на основе FragmentProvider, извлекает фрагмент из текущего Dagger-скоупа, и снабжает им команду.

## Команды

Все команды [NavigationCommand][navcom] разделены по классам экранов:

- [ActivityNavigationCommand][acom]
    * [Start][startcom] - старт новой Activity
    * [Finish][finishcom] - закрытие текущей открытой Activity.
    * [FinishAffinity][finishacom] - закрытие текущего таска Activity.
    * [Replace][replaceacom] - замена текущей Activity на другую (Finish + Start).
- [FragmentNavigationCommand][fcom]
    * [Add][addcom] - добавление фрагмента поверх текущего. Операция сохраняется в бекстек.
    * [Replace][replacefcom] - замена текущего фрагмента новым. Операция сохраняется в бекстек.
    * [ReplaceHard][replacehardcom] - замена текущего фрагмента новым с одновременным закрытием текущего. Аналогична двум операциям:
    remove + add, и выполняется за одну транзакцию.
    Если у нас был стек из фрагментов A, B и мы выполняем ReplaceHard(C), то новым стеком будет A, C.
    * [Remove][removecom] - удаление фрагмента из контейнера. Операция невозвратная.
    * [RemoveLast][removelastcom] - удаление фрагмента, располагающегося на вершине бекстека,
    и показ предыдущего. Является обратной операцией к Add/Replace, и выполняет обратные анимации.
    Чтобы выполнить эту команду для [TabFragmentNavigator][tfnav], необходимо явно указать параметр isTab=true.
    * [RemoveUntil][removeuntilcom] - удаление фрагментов вплоть до указанного в команде.
    * [RemoveAll][removeallcom] - очистка бекстека.
    Возможна очистка всех фрагментов с сохранением последнего: необходимо явно указать параметр shouldRemoveLast=false.
    Чтобы выполнить эту команду для [TabFragmentNavigator][tfnav], необходимо явно указать параметр isTab=true.
- [DialogNavigationCommand][dcom]
    * [Show][showcom] - показ диалога
    * [Dismiss][dismisscom] - скрытие диалога

## Анимации

Для того, чтобы осуществить какую-либо команду с анимацией, необходимо передать в параметр Animations
вашу анимацию, которую сможет обработать навигатор.
Стандартные навигаторы поддерживают обработку [BaseResourceAnimations][resanim], [SharedElementAnimations][sharedanim] и
SetAnimations[setanim], служащий контейнером для первых двух.

Для того, чтобы применить одну базовую анимацию сразу для всех команд всего приложения,
вы можете переопределить значения из класса [DefaultAnimations][defanim]. В нем содержатся анимации по-умолчанию
для каждого типа экрана.

## Табы

Модуль поддерживает навигацию по табам (BottomNavigation). Для того, чтобы ваш экран поддерживал навигацию по табам
и работал через [TabFragmentNavigator][tfnav], вам необходимо явно указать, что контейнер для навигации - это
[TabFragmentNavigationContainer][tfnavcontainer].
После этого, навигация в этом контейнере будет автоматически осуществляться через табы (TabFragmentNavigator).

Чтобы Route был вершиной таба (первым элементом, который находится в табе, и единственным, который сохраняется при очистке стека),
необходимо унаследовать его от [TabHeadRoute][tabheadroute].

Механизм навигации по табам работает следующим образом:

TODO: Расписать подробнее механизм навигации по табам.

## Подключение

Gradle:
```
    implementation "ru.surfstudio.android:navigation:X.X.X"
```

## Инициализация компонентов

Для того, чтобы команды навигации выполнялись и обрабатывались, вам нужно
инициализировать CommandExecutor в Application-классе вашего приложения:

1. Создайте ActivityNavigationProvider. Вы можете использовать свою реализацию, либо готовый
ActivityNavigationProviderCallbacks.
Во втором случае, не забудьте зарегистрировать коллбеки в Application.

1. Если вы используете модуль [navigation-observer](../lib-navigation-observer), инициализируйте ScreenResultBus.
Для него потребуется хранилище ScreenResultStorage, можете использовать FileScreenResultStorage с директорией
noBackupFilesDir.

1. Создайте AppCommandExecutor: если вы используете модуль navigation-observer, это будет
`AppCommandExecutorWithResult(screenResultBus, activityNavigationProvider)`, иначе -
`AppCommandExecutor(activityNavigationProvider)`.

1. Полученный CommandExecutor вы можете поместить в статическую переменную (если не используете DI),
либо предоставить как singletone-зависимость в вашем DI-фреймворке.

Для примера инициализации без какого-либо DI-фреймворка, вы можете посмотреть на
реализацию класса App из [Navigation sample](../sample-core-mvp).

Для примера инициализации с использованием Dagger 2, вы можете посмотреть на
реализаци класса App из [Navigation surf sample](../sample-standard).

## Использование

### Открытие экрана (Activity)

Для открытия Activity `activityA` нам потребуется:
1. Создать `RouteA`, которая будет содержать все параметры, необходимые для инициализации и идентификации экрана.
Унаследовать эту Route от [ActivityRoute][aroute].
    * Переопределить `getScreenClass`, если route лежит в одном модуле с экраном
    * Переопределить `getScreenClassPath`, если route лежит в другом модуле, и не имеет прямого доступа к экрану
1. Вызвать метод `NavigationCommandExecutor.execute` и передать в него команду [Start][startcom](routeA).

### Открытие экрана (Dialog)

Осуществляется аналогично открытию Activity, только route наследуется от [DialogRoute][droute],
и вместо команды Start используется команда Show[showcom].

### Открытие экрана (Fragment)

Для открытия `fragmentA` нам потребуется:
1. Создать `RouteA`, которая будет содержать все параметры, необходимые для инициализации и идентификации экрана.
Унаследовать эту Route от [FragmentRoute][froute].
    * Переопределить `getScreenClass`, если route лежит в одном модуле с экраном
    * Переопределить `getScreenClassPath`, если route лежит в другом модуле, и не имеет прямого доступа к экрану
1. Унаследовать класс-родитель, содержащий ViewGroup-контейнер для отображения фрагмента, от
интерфейса  [FragmentNavigationContainer][fnavcontainer] и задать полю `containerId` значение с id этой ViewGroup.
1. Извлечь тег экрана, открывающего этот Fragment (`route.getTag` / `fragment.getTag`).
    * Если экран открывает activity, этот шаг можно опустить.
    * Если в качестве NavigationCommandExecutor вы используете ScreenScopeCommandExecutor, этот шаг можно опустить.
1. Вызвать метод NavigationCommandExecutor.execute и передать в него команду Add(routeA, sourceTag),
где routeA - route, созданный на первом шаге, sourceTag - тег экрана, созданный на 3 шаге.


[route]: src/main/java/ru/surfstudio/android/navigation/route/Route.kt
[baseroute]: src/main/java/ru/surfstudio/android/navigation/route/BaseRoute.kt
[aroute]: src/main/java/ru/surfstudio/android/navigation/route/activity/ActivityRoute.kt
[froute]: src/main/java/ru/surfstudio/android/navigation/route/fragment/FragmentRoute.kt
[droute]: src/main/java/ru/surfstudio/android/navigation/route/dialog/DialogRoute.kt
[wroute]: src/main/java/ru/surfstudio/android/navigation/route/widget/WidgetRoute.kt
[tabroute]: src/main/java/ru/surfstudio/android/navigation/route/tab/TabRoute.kt
[tabheadroute]: src/main/java/ru/surfstudio/android/navigation/route/tab/TabHeadRoute.kt

[anav]: src/main/java/ru/surfstudio/android/navigation/navigator/activity/ActivityNavigator.kt
[fnav]: src/main/java/ru/surfstudio/android/navigation/navigator/fragment/FragmentNavigator.kt
[tfnav]: src/main/java/ru/surfstudio/android/navigation/navigator/fragment/tab/TabFragmentNavigator.kt
[fnavcontainer]: src/main/java/ru/surfstudio/android/navigation/provider/container/FragmentNavigationContainer.kt
[tfnavcontainer]: src/main/java/ru/surfstudio/android/navigation/provider/container/TabFragmentNavigationContainer.kt
[dnav]: src/main/java/ru/surfstudio/android/navigation/navigator/dialog/DialogNavigator.kt

[exec]: src/main/java/ru/surfstudio/android/navigation/executor/CommandExecutor.kt
[appexec]: src/main/java/ru/surfstudio/android/navigation/executor/AppCommandExecutor.kt

[aprov]: src/main/java/ru/surfstudio/android/navigation/provider/ActivityNavigationProvider.kt
[fprov]: src/main/java/ru/surfstudio/android/navigation/provider/FragmentNavigationProvider.kt
[aprovcal]: src/main/java/ru/surfstudio/android/navigation/provider/callbacks/ActivityNavigationProviderCallbacks.kt
[fprovcal]: src/main/java/ru/surfstudio/android/navigation/provider/callbacks/FragmentNavigationProviderCallbacks.kt


[anim]: src/main/java/ru/surfstudio/android/navigation/animation/Animations.kt
[resanim]: src/main/java/ru/surfstudio/android/navigation/animation/resource/BaseResourceAnimations.kt
[sharedanim]: src/main/java/ru/surfstudio/android/navigation/animation/shared/SharedElementAnimations.kt
[setanim]: src/main/java/ru/surfstudio/android/navigation/animation/set/SetAnimations.kt
[defanim]: src/main/java/ru/surfstudio/android/navigation/animation/DefaultAnimations.kt

[navcom]: src/main/java/ru/surfstudio/android/navigation/command/NavigationCommand.kt

[acom]: src/main/java/ru/surfstudio/android/navigation/command/activity/base/ActivityNavigationCommand.kt
[startcom]: src/main/java/ru/surfstudio/android/navigation/command/activity/Start.kt
[finishcom]: src/main/java/ru/surfstudio/android/navigation/command/activity/Finish.kt
[finishacom]: src/main/java/ru/surfstudio/android/navigation/command/activity/FinishAffinity.kt
[replaceacom]: src/main/java/ru/surfstudio/android/navigation/command/activity/Replace.kt

[fcom]: src/main/java/ru/surfstudio/android/navigation/command/fragment/base/FragmentNavigationCommand.kt
[addcom]: src/main/java/ru/surfstudio/android/navigation/command/fragment/Add.kt
[replacefcom]: src/main/java/ru/surfstudio/android/navigation/command/fragment/Replace.kt
[replacehardcom]: src/main/java/ru/surfstudio/android/navigation/command/fragment/ReplaceHard.kt
[removecom]: src/main/java/ru/surfstudio/android/navigation/command/fragment/Remove.kt
[removeuntilcom]: src/main/java/ru/surfstudio/android/navigation/command/fragment/RemoveUntil.kt
[removelastcom]: src/main/java/ru/surfstudio/android/navigation/command/fragment/RemoveLast.kt
[removeallcom]: src/main/java/ru/surfstudio/android/navigation/command/fragment/RemoveAll.kt

[dcom]: src/main/java/ru/surfstudio/android/navigation/command/dialog/base/DialogNavigationCommand.kt
[showcom]: src/main/java/ru/surfstudio/android/navigation/command/dialog/Show.kt
[dismisscom]: src/main/java/ru/surfstudio/android/navigation/command/dialog/Dismiss.kt
