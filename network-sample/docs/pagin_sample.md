# Сэмпл для демонстрации работы с пагинацией

Основные классы для работы с пагинацией:

- DataList(на выбор в зависимости от [механизма на сервере](../../docs/common/pagin.md)):
    - [DataList c поддержкой limit-offset](../../datalist-limit-offset/README.md);
    - [DataList с поддержкой page-count](../../datalist-page-count/README.md);
- [класс-наследник базового адаптера для пагинации](../src/main/java/ru/surfstudio/android/network/sample/ui/screen/main/list/ProductListAdapter.kt).
Нужен для настройки футера.
- [класс-наследник ..Pgn..ScreenModel](../src/main/java/ru/surfstudio/android/network/sample/ui/screen/main/MainScreenModel.kt).
Поддерживает установку состояния пагинации.


В данном сэмпле реализован пагинируемый запрос на основе механизма **page-count**.

Классы описывающие апи и соответствующие уровню интерактор [здесь](../src/main/java/ru/surfstudio/android/network/sample/interactor/product).
Но в реальном случае, загруженные данные должны быть также обернуты в DataList,
проинициализированный мета-данными с сервера(смещение, страница и тп).

Работа с пагинацией происходит в [презентере](../src/main/java/ru/surfstudio/android/network/sample/ui/screen/main/MainPresenter.kt).
Смещение следующего блока данных(или его страницу) берем через соответствующие
методы у DataList.
Загрузку проводим в методе loadMore(), который будет вызываться в колбеке у
адаптера на view.
Загруженный DataList сливаем с текущим через метод `#merge()`.

**Внимание**: не используем стандартные методы для списков типа addAll()!

Состояние пагинации устанавливается не напрямую, а через соответсвующие методы
ScreenModel: `#setNormalPaginationState()` и `setErrorPaginationState()`.
В первый подается флаг возможности загрузки в список(обусловлен текущим количеством эдементов и totalCount).
В завсисимости от него будут два состояния:
* COMPLETE - загрузка завершена, дальнейшей подгрузки не будет.
* READY - загрузка возможна;

После вызова onLoadMore() у адаптера его надо явно привести в состояние READY.
Это делается вызовом установкой соответсвующего состояния у ScreenModel
после успешной загрузки данных.
