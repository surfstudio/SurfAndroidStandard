# Пагинация


Пагинация(постраничная загрузка данных) может быть реализована двумя механизмами:
- limit-offset - известен размер блока данных и смещение относительно самого
первого элемента

- page-count - известен размер страницы и ее номер.

Выбор какой механизм использовать обусловлен скорее серверной стороной.

#### Структура данных
Для поддержки пагинации на клиенте предусмотрены структуры данных,
предоставляемые одним из следующих модулей:
- [datalist-limit-offset](../../datalist-limit-offset/README.md);
- [datalist-page-count](../../datalist-page-count/README.md).


####
Для реализации пагинации в RecyclerView предусмотрен класс
[BasePaginatioableAdapter](../easyadapter/src/main/java/ru/surfstudio/android/easyadapter/pagination/BasePaginationableAdapter.java)
