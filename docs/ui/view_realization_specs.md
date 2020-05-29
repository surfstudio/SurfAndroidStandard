[Главная](../main.md)

##### Особенности реализации View

- [Списки](#списки)
- [Изображения](#изображения)
- [Прочее](#прочее)

###### Списки
**RecyclerView.Adapter** - в качестве адаптера следует использовать [`EasyAdapter`][easy]
(не расширяя его). Если необходимо реализовать стики-хедеры(заголовки, прилипающие к
верхней границе списка) - использовать [`StickyEasyAdapter`][sticky]

**RecyclerView.ViewHolder** должен полностью скрывать все детали android.View,
которую он содержит. Для этого следует:

* Использовать ID ресурса верстки элемента списка внутри конструктора

* Использовать метод bind(data) для отрисовки данных на элементе списка

###### Изображения

**Загрузка изображений** происходит через класс [ImageLoader][imageloader],
который является оберткой над [`Glide`][glide].
**Использование этой абстракции позволит легко заменить Glide на любую
другую библиотеку**

**Ресурсы изображений**:
- Для всех иконок используется векторная графика. Для использования векторных
ресурсов нужно указывать атрибут `app:srcCompat` вместо `src`.
Векторные ресурсы также используются как иконки в меню тулбара.
Для того чтобы использовать вектора для других случаев,
например `drawableLeft`, то его следует обернуть в `layer-list`.
Запрещается использовать класс `VectorDrawable` для работы с векторными
изображениями в `runtime` - использовать следует `VectorDrawableCompat`.
[Гайд по использованию векторов](https://developer.android.com/studio/write/vector-asset-studio.html#running);

- Background всех `Button` и `FlatButton` сделан через `AppCompat` стили;

- Все *.png ресурсы только в разрешении xxhdpi, эти ресурсы андроид будет
масштабировать автоматически на более низкие разрешения + переводим в webp;

- Использовать `<shape>` предпочтительнее чем svg и png ресурсы.

###### Прочее
* [**MessageController**][message] - позволяет показывать `SnackBar`.
**Не может использоваться в презентере.**
Может использоваться `View` экрана и дополнительными сущностями скоупа `@PerScreen`,
например `StandardErrorHandler`;

* **Любые локальные действия** на экране с *AndroidFramework* и которые не относятся
к зоне ответственности Вью должны быть обернуты в соответствующие классы с интерфейсом,
**независящим** от фреймворка и поставляемым в презентер через *Dagger*.
(например проверка наличия модуля GCM или подписка на [`BroadcastReceiver`][broadcast])

* Вместо нативного ProgressBar используем MaterialProgressBar из этой библиотеки
(для стилизации элемента на prelollipop-устройствах).

* [PlaceHolderViewContainer](../../custom-view/lib-custom-view/src/main/java/ru/surfstudio/android/custom/view/placeholder/PlaceHolderViewContainer.kt) - кастомизируемый плейсхолдер
для различных лоад-стейтов.


[broadcast]: ../../broadcast-extension/README.md
[message]: ../../message-controller/README.md
[sticky]: ../../recycler-extension/README.md
[easy]: ../../easyadapter/lib-easyadapter/
[imageloader]: ../../imageloader/README.md
[glide]: https://github.com/bumptech/glide
