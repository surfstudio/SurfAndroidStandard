[TOC]

# Release Notes

## 0.4.0 - SNAPSHOT

#### toolbar-config

* ANDDEP-593 Добавлен ToolbarConfig

#### custom-view

* ANDDEP-571 Убран двойной сеттинг setTextAppearance у TitleSubtitleView

#### activity-holder

* Отдельный модуль для `ActiveActivityHolder`


#### analytics
* ANDDEP-294 Доработан модуль аналитики
* Поддержка любых параметров события аналитики. Возможность отправлять событие только в некоторые аналитики или по условиям.
* Старые события работают без изменений. 
#### animations
* ANDDEP-391 Добавлен параметр `defaultAlpha` для функций `fadeIn(...)` и `fadeOut(...)`.
Он сохраняет постоянную прозрачность для View при одновременном множественном вызове анимаций.
#### app-migration
#### broadcast-extension
#### connection
#### converter-gson

#### core-app

* Модуль - **deprecated**
* `CoreApp` - deprecated, при необходимости создайте на конкретном проекте.
* `ActiveActivityHolder` перенесен в модуль activity-holder
* `DefaultActivityLifecycleCallbacks` deprecated, реализация по-умолчанию находится в template-модуле
* `StringsProvider` deprecated, реализация по-умолчанию находится в template-модуле
* `Unit` - deprecated, используйте kotlin.Unit.

#### core-mvp
* ANDDEP-320 Возможность динамически задавать LoadState, гибкий способ отображения LoadState
* `BasePresenter` - добавлена возможность получать только последнее значение из `ObservableOperatorFreeze` с помощью метода 
`subscribeTakeLastFrozen`.

#### core-mvp-binding
* ANDDEP-575 В BaseRxPresenter добавлены экстеншны для rx-запросов
* ANDDEP-580 Поддержка биндинга в простых Bottom Sheet диалогах
* Исправлена ошибка, при которой игнорировались необработанные исключения. 
* Добавлены расширения для работы с биндингами
* Добавлена возможность подписки на State презентору
* Добавлена возможность подписки на Command презентору
* Добавлена поддержка ковариантности базовых rx классов при связывании.

#### core-mvi
#### core-ui

* добавлена возможность задать текст у кнопок стандартного диалога перехода в настройки.
* Исправлено локальное подключение модулей стандарта 
* Исправлен баг с использованием виджетов в recyclerView когда WidgetViewDelegate не успевал обнулять view у презентера
* Добавлена защита от модификации извне для списка делегатов в методе `resolve` класса `MultipleScreenEventResolver`

#### custom-view

* Исправлено некорректное поведение MaterialProgressBar на Android версии 5 и ниже
* Добавлено BottomSheetView

#### dagger-scope
#### datalist-limit-offset

* ANDDEP-242 Добавлена возможность принудительной фильтрации в методе DataList.merge по настраиваемому критерию для устранения одинаковых элементов 
* ANDDEP-242 Добавлены extension-функции для работы с DataList как с коллекцией: emptyDataListOf, DataList.filter, DataList.map

#### datalist-page-count

* ANDDEP-242 Добавлены extension-функции для работы с DataList как с коллекцией: emptyDataListOf, DataList.map

#### easyadapter
* ANDDEP-270 Добавлена поддержка асинхронного инфлейта view в ViewHolder

#### filestorage
#### firebase-analytics
#### imageloader
* ANDDEP-306 Добавлена поддержка SVG для ImageLoader'а
#### location

* Исправлена потеря callback'а `LocationErrorResolution` во фрагменте

#### logger
#### message-controller
#### mvp-dialog
#### mvp-widget

* Добавлена возможность использовать виджеты в динамической верстке.
    Ликвидирована необходимость вызывать `init()` для инициализации виджета.
* Для использования в ресайклере необходимо в использовать ручной режим инициализации.
    Для этого создан атрибут `enableManualEdit`. Также в `onBindViewHoler` необходимо вызывать `init(scopeId)`, 
    где scopeId должен быть основан на тех данных, которые отображает айтем.
* ANDDEP-380 Обновление работы виджетов: теперь за получение
    уникального идентификатора виджета отвечает метод `getWidgetId`.
    Вместо `init(scopeId)` в `RecyclerView` следует
    использовать `lazyInit`, и переопределить `getWidgetId` на основе данных из `onBindViewHolder`.
    Для расположения виджетов в статической верстке
    необходимо указывать уникальный `android:id`.
* Добавлена поддержка биндинга в виджетах
* Решена проблема с получением контекста у виджетов, лежащих внутри контейнера с атрибутом theme
* ANDDEP-573 - Добавлен биндинг для виджетов
* Решена проблема с повторным вызовом onFirstLoad при использовании виджетов внутри RecyclerView
* Удалена необходимость явного вызова lazyInit у виджетов в RecyclerView.
* Удалены `@Deprecated`-методы `init()`, `init(String scopeId)`. Вместо них следует использовать метод `lazyInit()`.
* Исправлено обнуление view у динамических виджетов в RecyclerView: фикс жизненного цикла, связанного с созданием двух ViewHolder для анимации одного элемента.
* Исправлено поведение, при котором при пересоздании экрана в методах презентера onLoad, onStart и onResume view была равна null.

#### network
#### picture-provider

#### push

* удален NotificationCenter - используйте `PushHandler`
* добавлена группировка пуш уведомлений
* теперь можно подписаться на такие события как открытия и отклонения пуш уведомления
* по умолчанию при клике на пуш уведомления вызывается колбек `PushEventListener`, чтобы поменять поведение можно переопределить preparePendingIntent у `PushHandlerStrategy`
* пофикшен NPE при конвертации `Intent.extras` в `HashMap` методом `convert` объекта `IntentPushDataConverter`

#### recycler-extension

* ANDDEP-266 Обновление DividerItemDecorator
    * Добавлена возможность задавать padding для разделителей
    * Параметр footerCount переименован в lastItemsCountWithoutDividers
    * Добавлена возможность задавать firstItemsCountWithoutDividers

#### rx-extension

* ANDDEP-579 Добавлена поддержка Single в ObservableUtil

#### rxbus
#### shared-pref
#### standard-dialog

#### templates

* обновлены file-шаблоны

#### template

* обновлены механизмы навигации в template
* ANDDEP-323 Добавлен Chuck
    * Интеграция Chuck в template
    * Добавлена возможность его включения/выключения на DebugScreen
* ANDDEP-336 Добавить TinyDancer на debug screen
    * Добавлена библиотека TinyDancer для отображения FPS
* ANDDEP-335 Добавлен Stetho на DebugScreen
    * Добавлена библиотека Stetho которая в связке с Google Chrome может использоваться для дебага.
* Добавлены метки версий на иконках приложения в лаунчере
* Добавлен переключатель между основным и тестовым сервером
* Добавлена возможность открыть Developer Tools через DebugScreen
* Добавлен LeakCanary
* Добавлен просмотрщик файлового хранилища приложения
* Добавлен плагин Build scans https://guides.gradle.org/creating-build-scans/
* Добавлена возможность добавить задержку выполнения запроса
    * На экране DebugScreen в настройках сервера можно добавить задержку запроса 0c 0.5c 1c 2c 4c 8c
* ANDDEP-444 Вынесены Dagger-зависимостей из [`AppComponent`](template/base_feature/src/main/java/ru/surfstudio/standard/application/app/di/AppComponent.kt)
и [`ActivityComponent`](template/base_feature/src/main/java/ru/surfstudio/standard/ui/activity/di/ActivityComponent.kt)
в отдельные классы:  [`AppProxyDependencies`](template/base_feature/src/main/java/ru/surfstudio/standard/application/app/di/AppProxyDependencies.kt)
и [`ActivityProxyDependencies`](template/base_feature/src/main/java/ru/surfstudio/standard/ui/activity/di/ActivityProxyDependencies.kt),
которые теперь отвечают за распределение зависимостей между компонентами.
* SBB-1862 Добавлен модуль cf-pagination

#### util-ktx

* ANDDEP-592 Добавлено в readme описания к BlockableData, CheckableData, DeletableData, ExpandableDataInterface, LoadableData, ScrollableData, SelectableData, VisibleData

* ANDDEP-319 Свойства `isAtLeast...`  класса [`SdkUtils`](util-ktx/src/main/java/ru/surfstudio/android/utilktx/util/SdkUtils.kt)
помечены как `@Deprecated`, вместо них следует использовать методы `isAtLeast...()`, а так же `runOn...()`.

#### connection
* Добавлен метод для проверки подключения через Wi-Fi в [`ConnectionProvider`](connection/src/main/java/ru/surfstudio/android/connection/ConnectionProvider.java)

#### mvp-widget

#### imageloader
* ANDDEP-317 Оптимизация [`ImageLoader`](imageloader/src/main/java/ru/surfstudio/android/imageloader/ImageLoader.kt):
    * Рефакторинг трансформаций, удаление обращений к рефлексии
    * Уход от работы на основе `SimpleTarget`, устранение утечек памяти, добавление возможности очищения памяти
    * Ускорение работы, добавление проверок на опциональное использование переменных
    * Добавление функций-расширений для опциональной работы с RequestBuilder [`ImageLoaderUtils.kt`](imageloader/src/main/java/ru/surfstudio/android/imageloader/util/ImageLoaderUtils.kt)
    * Добавление расширенного списка стратегий кеширования [`CacheStrategy`](imageloader/src/main/java/ru/surfstudio/android/imageloader/data/CacheStrategy.kt)
* ANDDEP-442 Расширение функционала [`ImageLoader`](imageloader/src/main/java/ru/surfstudio/android/imageloader/ImageLoader.kt): 
    * Добавлена поддержка устанавливать Tile изображению (мостить по горизонтали и вертикали)
    * Изменен метод `ImageLoader.mask`, теперь он принимает параметром `PorterDuff.Mode` для установки необходимого типа заливки
    * Добавление списка источников загрузки изображения [`ImageSource`](imageloader/src/main/java/ru/surfstudio/android/imageloader/data/ImageSource.kt)
    * Добавление listener'a с источником загрузки изображения: `ImageLoader.listenerWithSource`
    * Исправление неочевидного поведения функции `ImageLoader.into`, добавление перегрузки с лямбдами-слушателями
    * Добавление возможности не применять трансформации к превью и ошибке. 

### security
* ANDDEP-82 Модуль для обеспечения безопасности
    * [AppDebuggableChecker](security-sample-template/security/src/main/java/ru/surfstudio/android/security/app/AppDebuggableChecker.kt)- класс, проверяющий debuggable-флаги приложения при его запуске.
    * [RootChecker](security-sample-template/security/src/main/java/ru/surfstudio/android/security/root/RootChecker.kt) - проверяет наличие рут-прав на устройстве.
    * [KeyEncryptor](security-sample-template/security/src/main/java/ru/surfstudio/android/security/crypto/KeyEncryptor.kt) - абстрактный класс для реализации безопасного [Encryptor'a](filestorage/src/main/java/ru/surfstudio/android/filestorage/encryptor/Encryptor.kt).
    * [CertificatePinnerCreator](security-sample-template/security/src/main/java/ru/surfstudio/android/security/ssl/CertificatePinnerCreator.kt) - класс, создающий CertificatePinner для OkHttpClient для реализации ssl-pinning.
    * [SessionManager](security-sample-template/security/src/main/java/ru/surfstudio/android/security/session/SessionManager.kt) - Менеджер для отслеживания сессии Activity.
    * [SecurityUiExtensions](security-sample-template/security/src/main/java/ru/surfstudio/android/security/ui/SecurityUiExtensions.kt) -  - Утилиты для реализаци безопасного UI.

    * Расписаны Security tips, которые необходимо учитывать в приложении.
* SBB-2530 Добавлен extension-метод для полной блокировки контекстного меню в поле ввода EditText.disableContextMenu()

## 0.3.0

#### core-ui

* ANDDEP-220 - исправлен баг `TabFragmentNavigator` с добавлением в бекстек
* [`PermissionManager`](core-ui/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionManager.kt):
    * переписан на Kotlin
    * `PermissionManagerFor...` изменились конструкторы - теперь принимают дополнительно: SharedPrefs и Navigator
    * Добавлен класс [`PermissionStatus`](core-ui/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionStatus.kt) -
        оборачивает ответ на проверку разрешения.

        Теперь `check(permissionRequest: PermissionRequest): PermissionStatus`.

    * Расширен функционал: новые поля в [`PermissionRequest`](core-ui/src/main/java/ru/surfstudio/android/core/ui/permission/PermissionRequest.kt),
    возможность настроить диалог об объяснении причин запроса разрешения,
    возможность показать необходимость перехода в настройки телефона.
* SBB-2523 добавлен интерфейс CrossFeatureFragment для навигации между фрагментами из разных feature-модулей.

#### camera-view

* ANDDEP-298 - Вынесение модуля camera-view в отдельный репозиторий

#### easyadapter

* ANDDEP-200 - Убрана рандомизация ViewType у ItemController
* `getItemId` у контроллера - возвращает String

#### filestorage

* Добавлены утилиты для получения разных папок [`AppDirectoriesProvider`](filestorage/src/main/java/ru/surfstudio/android/filestorage/utils/AppDirectoriesProvider.kt)
* Изменение `CacheConstant`. Теперь подразделяются на:
    * `INTERNAL_CACHE_DIR_DAGGER_NAME == NO_BACKUP_STORAGE_DIR_NAME, BACKUP_STORAGE_DIR_NAME`
    * `EXTERNAL_CACHE_DIR_DAGGER_NAME == CACHE_DIR_NAME`

#### location

* ANDDEP-21 - создан модуль для локации

#### logger

* ANDDEP-222 - добавлена возможность выбрать стратегию для логгирования

#### mvp-binding

* Переименован `onViewDetached()` -> `onViewDetach()`

#### mvp-dialog

* ANDDEP-243 - Исправление SimpleDialogDelegate - фикс неверного ключа

#### push

* NotificationCenter - устарел
* Основным является `PushHandler` и его реализация
* Теперь существует возможность конфигурировать помощник через даггер.
* Существует возможность подписаться на пуш через `PushInteractor`
* Добавлен `FcmStorage`

#### recycler-extension

* Изменен конструктор `StickyLayoutManager`
* Удален `StickyBindableItemController`
* Добавлен `StickyFooter` и соответсвующий контроллер
* Добавлен `StickyHeader` и соответсвующий контроллер

#### rx-bus

* `RxBus` вынесен в отдельный модуль

#### picture-provider

* ANDDEP-235 - рефакторинг и добавление функцмонала:
    * Кроме перехода сразу в галерею, появилась возможность выбрать файл и файлового менеджера
    * Добавлен метод для предварительного сохранения изображения
    * Добавлен класс-обертка над Uri
* Фикс получения разрешения на доступ к камере
* ANDDEP-286 - Исправление работы с remote-изображениями у PictureProvider
* Заменены возвращаемые типы Observable -> Single

#### utilktx

* ANDDEP-258 - добавлены toggle-методы у основных оберток(`CheckableData`, `SelectableData`)
* Добавлены расширения для работы с ClipboardManager :
    * `copyTextToClipboard()` - копирует текст в буфер обмена
* ANDDEP-211 добавлена возможность настраивать сдвиг часового пояса
    и получать дефолтный для устройства
* Добавлен ActivityLifecycleListener для удобного использования Application.ActivityLifecycleCallbacks

#### template

* ANDDEP-272 - DebugScreen
* ANDDEP-250 - вынесены типы сборок в отдельный грэдл-файл
* ANDDEP-254 - добавлен механизм подписи приложения - директория keystore.
* ANDDEP-255 - создано минимальное тестовое окружения для тестирования
    без эмулятора(Robolectric)
* Добавлена возможность подключать модули локально. Описание [здесь](template/android-standard/README.md)


#### общее

* ANDDEP-221 - обновлены версии используемых библиотек
* ANDDEP-230 - добавлены сэмплы на модули
* ANDDEP-219 - проведена оптимизация импортов
* ANDDEP-275 - фикс билда на студии 3.2 из-за AAPT2
* ANDDEP-218 - добавлена основная документация к модулям
* ANDDEP-231 - правила работы с Dagger в студии Surf
* ANDDEP-302 - Изменены вызовы doOnDispose() на doFinally
* ANDDEP-308 - Понижение minSdkVersion до 17
* Удалены Dagger-зависимости из модулей
* Теперь для подключения двух модулей, где первый зависит от второго,
    необходимо подключить только первый. Зависимости подтянутся автоматически.


## 0.2.2-SNAPSHOT
* Добавлен экспериментальный модуль Core mvp binding
* В PictureProvider добавлен метод возвращающий Uri(вместо реального пути) выбранного файла
* Испрвлен package name для TitleSubtitleView


## 0.2.1
* ANDDEP-137 Добавить возможность изменять errorHandler в basePresenter
* ANDDEP-138 Доработки PlaceHolderView
    * Добавлен набор индикаторов загрузки
    * Добавлено новое состояние LoadState "No Internet";
    * Добавлены атрибуты `pvProgressBarWidth` и `pvProgressBarHeight` - через них можно задавать ширину и высоту прогресс-индикатора соответственно;
    * Добавлены атрибуты `pvOpaqueBackground` и `pvTransparentBackground` - через них можно задавать drawable-ресурсы в качестве фона;
    * Появилась поддержка 28-и кастомных прогресс-индикаторов `pvProgressBarType`;
    * Атрибут `pvProgressBarColor` теперь принимает не только ссылки на цветовые ресурсы, но и коды вида `#00FFAA`;
    * Правка гравитации подписей
    * Добавлены атрибуты `pvTitleLineSpacingExtra` и `pvSubtitleLineSpacingExtra` для изменения высоты строки заголвоков
    * Правка проблем с перехватом жестов на плейсхолдере
* ANDDEP-140 TextViewExtensions добавлены публичные методы:
    * EditText.selectionToEnd() - перевод каретки в конец
    * EditText.allowMatch(predicate) - фильтрация символов
    * EditText.restrictMatch(predicate) - // -
* ANDDEP-50 исправлены задержки переключения состояний с в PlaceHolderView
* ANDDEP-142 исправлен баг с неправильным добавлением элемента в методе ItemList.addStickyHeaderIf()
* ANDDEP-145 Добавлена в BasePaginationableAdpater поддержка StaggeredGrid
* TitleSubtitleView - отключение Clickable при установке пустого листенера, мелкие правки
* ANDDEP-148 BaseCallAdapterFactory - добавлена поддержка Flowable, Maybe, Single, Completable
* AnimationUtil - правки анимаций FadeIn/FadeOut
* ANDDEP-79 Добавлены распространенные классы и интерфейсы обертки над данными: BlockableData, CheckableData, DeletableData, ExpandableData, LoadableData, ScrollableData, SelectableData, VisibleData и методы расширения на их коллекции
* ANDDEP-194 BaseCallAdapterFactory добавлена возможность повторить неудавшийся запрос через метод доступный в предке класса
* ANDDEP-197 добавлен модуль broadcast-extension c RxBroadcastReceiver и BaseSmsBroadcastReceiver
* ANDDEP-199 PictureProvider переход сразу в галерею (изменен роут, убран Chooser)
* ANDDEP-198 Добавлены метод shouldShowRequestPermissionRationale в PermissionManager, позволяющий понять показывается ли еще диалог запроса пермишена или он уже запрещен
* ImageLoader -
    * Поднятие версии Glide до 3.7.1
    * Правка оптимизации
    * Исправлена загрузка в SimpleTarget
    * Добавлен downSampling
    * Добавлена возможность принудительного обновления изображения с помощью метода force()
    * ANDDEP-212 Добавлен метод crossFade для плавного рендеринга изображений
* MessageController
    * ANDDEP-210 добавлен параметр - длительность показа снека
    * ANDDEP-210 Добавлена возможность убирать снек, важно при наличии снеков, которые не уходят сами
* Исправлено падение IllegalStateException "Can't find Persistent scope ..." при переходе с восстановленного экрана, содержащего фрагменты в состоянии detached
* ANDDEP-213 Исправлено закрытие диалога StandardDialog если установлен флаг isCancelable
* ANDDEP-215 Перевод всех диалогов на AppCompatDialog


## 0.2.0
* Fix сравнения путей для SimpleCache
* ANDDEP-127 Добавлена поддержка слияния data-list с 2х сторон
* ANDDEP-136 fix не логгируются http запросы
* ANDDEP-107 Исправления PlaceholderView
* ANDDEP-125 sticky header fix
* ANDDEP-109  правка StandardDialogRoute
* ANDDEP-108  Сделать TabFragmentNavigator через detach/attach