[Главная](../../main.md)

# Загрузка основных данных экрана и управление состояниями

- [Загрузка основных данных](#загрузка-основных-данных)
  - [1. Придерживаться правильного именования методов и их параметров](#1-придерживаться-правильного-именования-методов-и-их-параметров)
  - [2. Не плодить большое количество методов `...load…()`](#2-не-плодить-большое-количество-методов-load)
  - [3. Избегать `boolean` параметры в методах `...load…()`](#3-избегать-boolean-параметры-в-методах-load)
  - [4. Observable как параметр в методах load…()](#4-observable-как-параметр-в-методах-load)
  - [5. Пагинация](#5-пагинация)


Самой сложной логикой в презентере чаще всего является загрузка основных
данных и управление состояниями.

## Загрузка основных данных
Этой логике следует уделять особое внимание, иначе можно получить совершенно
неподдерживаемую логику, например, это метод из реального проекта,
который перезагружает данные экрана:

![](https://preview.ibb.co/ihvYaU/before_refactor.png)

A это он же, но после рефакторинга:

![](https://image.ibb.co/jEko9p/after_refactor.png)

Часто такой запутанный код возникает, когда экран несколько раз дорабатывался
(это может быть и исправление нескольких багов в контексте одного спринта,
так и переделки логики в разных спринтах), причем при каждой доработке
разработчик не полностью разбирался, как работает существующий код.
В результате чего возникал снежный ком - некачественное изменение кода
или даже написание его с нуля приводит к увеличению сложности, что может
спровоцировать другого (или того же) разработчика при следующей правке на еще
одно некачественное изменение, и так далее, пока не получим логику (как на первой схеме),
с которой уже никто не сможет нормально работать.

Для упрощения этой логики следует пользоваться следующими принципами:


*Замечание*: В качестве примеров могут приводиться методы, которые не
следует использовать как полноценное руководство, примеры приведены только
для демонстрации. Для конкретно вашего случая может потребоваться другая
структура методов, но принципы, по которым построена ваша логика, должны
соответствовать принципам, приведенным в этой статье.

### 1. Придерживаться правильного именования методов и их параметров
Как в примере выше метод `tryReloadData()` перезагружает все данные, а
`loadData` только продукты, что явно вводит в заблуждение. Еще некоторые
методы из примера имели `bool` параметр `takeLast` что означает, что нужно
получить данные с сервера игнорируя кеш, таким образом более понятное
название этого параметра будет, к примеру, `onlyFromServer`.

### 2. Не плодить большое количество методов `...load…()`

В примере выше до рефакторинга было около **10 таких методов**. После рефакторинга 6:
* 3 основных метода загрузки, по одному на каждый тип данных (Product,Basket, Folders)
* 2 метода обновления данных:

    - reloadProductsPortions() - перезагружает все ранее загруженные продукты
(хотя и от этого метода можно было отказаться, так как он содержит всего
одну строчку `loadProducts(getProductsRequestPortions(onlyFromServer),
clearProductsOnSuccessAction);`, но он был сделан из-за того, что он
часто используется в коде и имеет очень точную зону ответственности)

    - reloadAllData() - перезагружает все данные

* 1 метод инициализации, `tryLoadProducts()` - загружает данные, если они
еще не были загружены

Причем новые методы образуют строгую и понятную иерархию: методы пункта “a”
соответствуют самому низкому уровню абстракции,
метод reloadAllData() относится к самому высокому. В контексте сказанного
стоит вспомнить выражение, что “любую проблему можно решить добавлением
еще одного уровня абстракции, за исключением проблемы излишнего количества
уровней абстракции”. То есть не стоит делать методы слишком вложенными,
чем более плоская структура, тем проще поддерживать.

Основной правило, из-за которого следует создавать метод `...load…()`,
это когда этот метод должен использоваться в нескольких местах.
Однако не стоит переиспользовать абсолютно всю логику, некоторые простейшие
вещи можно продублировать. Например, при изменении выбранной папки или
изменении фильтра следует очистить старые данные и запустить загрузку
новых данных. Обработчик каждого из этих событий содержит специфическую
логику этого события, логику очистки старых данных и и вызов метода `loadProducts()`.
Хотя получается, что логика очистки дублируется, однако читать и поддерживать
такой код проще из-за его невысокой вложенности.

### 3. Избегать `boolean` параметры в методах `...load…()`
По возможности лучше вообще их избегать, в примере выше удалось после
рефакторинга убрать большую их часть. Однако есть случаи, когда нужно иметь
возможность менять логику метода, в таком случае лучше использовать в
качестве параметров функции другую функцию вместо boolean переменной. Например: метод
```
void loadProducts(Observable<DataList<Product>> request, boolean clearPreviousDataOnSuccess){
…
}
```
может быть переписан с сигнатурой:
```
void loadProducts(Observable<DataList<Product>> request, Action0 onSuccessExtraAction){
…
}
```

Соответственно использование этих методов в коде будет выглядеть примерно так:
```
loadProducts(getProductsRequest(..), false); //первый метод

loadProducts(getProductsRequest(..), clearProductsOnSuccessAction); //второй метод
loadProducts(getProductsRequest(..), noAction); //второй метод
```

Как видно использование функций в качестве параметров делает код более
декларативным. К тому же этот подход более гибкий, можно подставлять дополнительные
действия, не меняя сигнатуры метода. Можно подставлять туда также ссылки на методы.

Можно также расширить этот принцип, сказав что лучше вообще по возможности
уменьшать количество параметров у методов ...load…() - меньше параметров,
меньше вариантов работы метода, которые нужно учитывать.

### 4. Observable как параметр в методах load…()

Как и предыдущий пункт, этот параметр делает метод более гибким, к примеру в функцию
loadProducts(Observable, Action) можно подставить как запрос, который загружает
только один блок данных, так и комбинированный запрос, загружающий несколько
блоков (используется для перезагрузки всех данных после пагинации). К тому
же метод может иметь несколько параметров Observable если требуется загружать
одновременно несколько типов данных. Такой подход позволяет также загружать
только нужную часть данных, передав в качестве ненужных Observable заглушки.


Еще лучше всего делать методы, которые возвращают нужный Observable.
Сравните 2 варианта использования метода loadProducts(Observable, Action):
```
loadProducts(basketRepository.getDeferredProducts(currentFolder.getId(), DEFAULT_LIMIT,
                        0, false, screenData.getProductsFilter(), false),
                        noAction);


loadProducts(getProductsRequest(DEFAULT_OFFSET), noAction);
```

В частности благодаря этому подходу можно избавиться от вложенности методов
...load..(), потому что иногда эта вложенность возникает из-за желания
скрыть детали использования метода loadProducts(Observable, Action) в стиле
первого варианта.
Еще у методов, возвращающих Observable, следует оставлять минимальное
количество параметров и избегать использование boolean параметров, чтобы
они меньше отвлекали. Для этого можно создавать несколько таких методов,
каждый с 0-2 параметрами и соответствующим названием.

### 5. Пагинация

Загрузку данных для след блока лучше производить отдельно от основного
метода загрузки данных, так как логика обработки ответа сильно отличается.




