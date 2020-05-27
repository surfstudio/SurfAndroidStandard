# Mvp-Binding sample

# Модуль с примерами использования

## Главный экан
Реализован с испоьзованием множественных презенторов.
На главном экране показаны примитивные операции: связывание виджетов и
данных без трансформации, реакция на события и работа с диалогом.

## Easyadapter
Описывает работу с `EasyAdapter`.

Основано на примере easyadapter с [githab`a][tuevSample]
Показательная логика по измененю состояния рецайклера через адаптер
представлена во [View][easyadapterView] и [Presenter][easyadapterPresenter]

### Основной экран
Демонстрирует заполнение `RecyclerView` с использованием
различных `ItemController`ов и возможность использования листнеров в
связке с `Relation` сущностями.

### Экран `PaginationActivityView`
Показывает работу
* с пагинируем списком данных с использованием `DataList`
* с состояниями
* с `SelectableData` — выбор элемента списка

## Экран `CheckboxActivityView`
Демонстрирует возможности комбинирования Action/State.

[tuevSample]: https://github.com/MaksTuev/EasyAdapter/tree/master/sample/src/main/java/ru/surfstudio/easyadapter/sample
[easyadapterView]: src/main/java/ru/surfstudio/android/mvp/binding/rx/sample/easyadapter/ui/screen/main/EAMainActivityView.kt
[easyadapterPresenter]: src/main/java/ru/surfstudio/android/mvp/binding/rx/sample/easyadapter/ui/screen/main/EAMainPresenter.kt

# Пример использования модуля (deprecated) [core-mvp-binding](../lib-core-mvp-binding/README.md)
