# MVP widget
Расширение модуля [core-mvp](../core-mvp) для работы со view
(в терминах android фреймвока) как со View(в терминах MVP).

Основные классы:

1. CoreFrameLayoutView
2. CoreLinearLayoutView
3. CoreRelativeLayoutView
4. CoreConstraintLayoutView

#Использование


### Подключение
Gradle:
```
    implementation "ru.surfstudio.android:mvp-widget:X.X.X"
```

### Использование

Для создания виджета необходимо сделать шаги, аналогичные сощданию экрана:
1. Наследоваться от одного из базовых виджетов
1. Расширить базовый презентер
1. Опционально: создать ScreenModel, и реализовать интерфейс RenderableView из модуля core-mvp
1. **Обязательно**: вызвать метод init во время onCreate() Activity или onActivityCreated() Fragment

**!! Важно !!**

Виджеты с презентером могут быть только в статической иерархии вью,
то есть должны создаваться при старте экрана, и не могут быть использованы при
динамическом создании вью, в том числе внутри элементов RecyclerView.