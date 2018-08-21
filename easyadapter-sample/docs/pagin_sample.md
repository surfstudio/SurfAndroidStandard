# Сэмпл для демонстрации работы с пагинацией

Пример реализации списка с пагинацией расположен в следующем пакете
 - [.ui.screen.common.pagination](../src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/pagination)

Основные классы для работы с пагинацией:

- DataList(на выбор в зависимости от [механизма на сервере](../../docs/common/pagin.md)):
    - [DataList c поддержкой limit-offset](../../datalist-limit-offset/README.md);
    - [DataList с поддержкой page-count](../../datalist-page-count/README.md);
- [класс-наследник базового адаптера для пагинации](../src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/pagination/PaginationableAdapter.kt).
Нужен для настройки футера.
- [класс-наследник ..Pgn..ScreenModel](../src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/pagination/PaginationListScreenModel.kt).
Поддерживает установку состояния пагинации.

В данном примере загрузка данных [имитируется](../src/main/java/ru/surfstudio/android/easyadapter/sample/interactor/FirstDataRepository.kt).
Но в реальном случае, загруженные данные должны быть также обернуты в DataList,
проинициализированный мета-данными с сервера(смещение, страница и тп).

Работа с пагинацией происходит в [презентере](../src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/pagination/PaginationListPresenter.kt).
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
