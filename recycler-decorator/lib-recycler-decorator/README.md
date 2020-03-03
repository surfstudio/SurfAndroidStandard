[Главная страница репозитория](/docs/main.md)

[TOC]

# RecyclerView decorator

Универсальный декоратор для работы с `RecycleView`.
Может быть использован как для отрисовки простых дивайдеров так и более сложных кейсов, например паралакс картинки на фоне ViewHolder или всей области RecyclerView.

# Использование
[Пример использования](/recycler-decorator/sample)

Данный декоратор представляет собой надстройку для класса RecyclerVIew.ItemDecoration.
Также он не исключает возможность использовать свои реализации RecyclerVIew.ItemDecoration.

# Цели
1. Удобная работа с декорированием RecyclerView, т.е уйти от написания RecyclerVIew.ItemDecoration под каждый нужный кейс.
2. Разделение сложной логики для отрисовки декора на более мелкие части.
3. Лекое комбинирования и переиспользование разных декораторов.
4. Простое управление последовательностью отрисовки декоров у RecyclerView.
5. Простота управления отрисовки декора над ViewHolder'ами и под ними.
6. Возможность привязывания декора к определенному типу ViewHolder.
7. Отделение кода отрисовки декора от кода выставления отсупов, для легкого комбинирования декора и отступов.
8. Легкая параметризация декораторов под нужды задачи, без переписывания логики отрисовки.
9. Прозрачность работы.

# Работа с декоратором

Работа декоратора построена на основе Builder'a.

```
val decorator = Decorator.Builder()
            .underlay(...)
            .underlay(...)
            .overlay(...)
            .offset(...)
            .offset(...)
            .offset(...)
            .build()
```

Подключается с RecyclerView как и обычный ItemDecoration

```
            recycler_view.addItemDecoration(decorator)
```
# Компоненты

Для реализации компонента для высталления отступов используется интерфейс *OffsetDecor*

```
    interface OffsetDecor {
        fun getItemOffsets(outRect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State)
    }
```

Для создания компонентов рисующих декор, используется два интерфейса,  
*RecyclerViewDecor* - нужен для отрисовки декора всего RecyclerView. Например полосы прокрутки.

*ViewHolderDecor* - является расширяет возможности RecyclerViewDecor.  
Нужен для отрисовки декора непосредственно элемента внутри RecyclerView.

Для удобства в метод draw поставляется View.
```
    interface RecyclerViewDecor {
        fun draw(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State)
    }
```
```
    interface ViewHolderDecor {
        fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State)
    }
```
# Методы Builder'a

Всего билдер имеет 4 метода для построения декоратора

*underlay* - сюда передаются все декораторы для отрисовки декора до отрисовки View внутри RecyclerView

*overlay* - сюда передаются все декораторы для отрисовки декора после отрисовки View внутри RecyclerView

*offset* - сюда передаются все декораторы для отрисовки отсупов у View внутри RecyclerView

*build* -  собирает все переданные декораторы и возвращает экземпляр *RecyclerVIew.ItemDecoration*

Методы *underlay*, *overlay* принимают реализации интерфейсов RecyclerViewDecor и ViewHolderDecor.

#### Связь с RecyclerView.ItemDecoration.
1. Все декораторы ViewHolderDecor, RecyclerViewDecor переденные в *underlay* отрисовываются в методе RecyclerView.ItemDecoration.onDraw(...)
2. Все декораторы ViewHolderDecor, RecyclerViewDecor переденные в *overlay* отрисовываются в методе RecyclerView.ItemDecoration.onDrawOver(...)
3. Все OffsetDecor переданные в методе *offset* отрисовываются в методе RecyclerView.ItemDecoration.getItemOffsets(...)

Исключения:
1. RecyclerViewDecor не может быть привязан к опредленному типу ViewHolder
2. OffsetDecor может быть только один для каждого типа ViewHolder
3. OffsetDecor переданный без привязки к типу ViewHolder может быть тоже только один.
4. Если в билдер передать OffsetDecor привязанный к определенному типу ViewHolder и OffsetDecor без привязки, то они могут исключить друг друга, т.к каждый выставит свои парамтеры отсупов для View


# Порядок отрисовки
Порядок отрисовки регулируется порядком методов underlay и overlay
До отрисовки ViewHolder'ов отрисоовываются (переденные через метод underlay):
1. Все RecyclerViewDecor
2. НЕ привязанные к itemViewType ViewHolderDecor
3. Привязанные к itemViewType ViewHolderDecor

После отрисовки ViewHolder'ов отрисовываются (переденные через метод overlay):
1. Привязанные к itemViewType ViewHolderDecor
2. НЕ привязанные к itemViewType ViewHolderDecor
3. Все RecyclerViewDecor

Отрисовка OffsetDecor:
1. Не привязанные к itemViewType
2. Привязанные к itemViewType

#### Пример 1
```
    .underlay(someViewHolder.itemViewType to someDecorDrawer1)
    .underlay(someViewHolder.itemViewType to someDecorDrawer2)
```
Это значит, что к *someViewHolder* привязыны *someDecorDrawer1* и *someDecorDrawer2*. И они отрисовываются в порядке, котором были переданы в билдер.  
*someDecorDrawer1* и *someDecorDrawer2* будут отрисовываться только при появлении *someViewHolder* в видимой области RecyclerView

#### Пример 2
Если передавать декоратор без привязки к определенному itemViewType то декоратор будет отрисовываться для всех itemViewType в списке RecyclerView
```
    .underlay(someDecorDrawer1)
    .underlay(someDecorDrawer2)
```
Данные примеры аналогично работают и для метода *overlay*


# Работа с EasyAdapter
1. Для привязки OffsetDecor и ViewHolderDecor к itemViewType, можно использовать метод контроллера *viewType()*
```
    .underlay(someController.viewType() to someDecorDrawer1)
    .underlay(someController.viewType() to someDecorDrawer2)
```
2. При необходимости из адаптера можно взять BaseItem для получения данных списка прям в декораторе
```
override fun draw(canvas: Canvas,
                      view: View,
                      recyclerView: RecyclerView,
                      state: RecyclerView.State) {

        val vh = recyclerView.getChildViewHolder(view)
        val adapter = recyclerView.adapter as EasyAdapter
        val baseItem = adapter.getItem(vh.adapterPosition)
        ...
}
```

Для более удобного подключения к EasyAdapter есть библиотека с расширениями
```
ru.surfstudio.android:recycler-decorator-ktx
```

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:recycler-decorator:X.X.X"
```