[TOC]

# Release Notes

## 0.4.0 - SNAPSHOT

#### push

* удален NotificationCenter - используйте `PushHandler`

#### templates

* обновлены file-шаблоны

#### util-ktx
* ANDDEP-319 Свойства `isAtLeast...`  класса [`SdkUtils`](util-ktx/src/main/java/ru/surfstudio/android/utilktx/util/SdkUtils.kt)
помечены как `@Deprecated`, вместо них следует использовать методы `isAtLeast...()`, а так же `runOn...()`.

#### connection
* Добавлен метод для проверки подключения через Wi-Fi в [`ConnectionProvider`](connection/src/main/java/ru/surfstudio/android/connection/ConnectionProvider.java)

#### mvp-widget
* Добавлены интерфейсы-маркеры состояний для Widget

#### imageloader
* ANDDEP-317 Оптимизация [`ImageLoader`](imageloader/src/main/java/ru/surfstudio/android/imageloader/ImageLoader.kt):
    * Рефакторинг трансформаций, удаление обращений к рефлексии
    * Уход от работы на основе `SimpleTarget`, устранение утечек памяти, добавление возможности очищения памяти
    * Ускорение работы, добавление проверок на опциональное использование переменных
    * Добавление функций-расширений для опциональной работы с RequestBuilder [`ImageLoaderUtils.kt`](imageloader/src/main/java/ru/surfstudio/android/imageloader/util/ImageLoaderUtils.kt)
    * Добавление расширенного списка стратегий кеширования [`CacheStrategy`](imageloader/src/main/java/ru/surfstudio/android/imageloader/data/CacheStrategy.kt)

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
    возможность показать необходимость перехода в настройки телефона;

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