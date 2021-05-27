# LoadState Concept
# Основы
LoadState - это определенное состояние экрана, самые распространненные:
  - MainLoading - загрузка, если на экране еще нет данных
  - TransparentLoading - загрузка поверх данных, обычно блокирует весь интерфейс
  - ErrorLoadState - состояние ошибки, если данные не пришли
  - EmptyLoadState - пустая выдача
  - NotFoundState - пустая выдача после применения фильтра

В качестве LoadState может выступать любой класс, реализующий интерфейс [LoadStateInterface][lsi]

LoadState хранится в ScreenModel (если та является наследником [LdsScreenModel][lsscm]), и устанавливается в Presenter
``` kotlin
screenModel.loadState = ErrorLoadState()
```

Экраны, наследующиеся от [BaseLdsActivityView][blav] и [BaseLdsFragmentView][blfv] должны содержать экземпляр класса,
ответственного за показ и смену стейтов на экране.
Таким классом может быть любой, реализующий интерфейс [LoadStateRendererInterface][lsri]
В семпле loadstates-sample приведен пример нескольких сущностей LoadStateRenderer (наследники [BaseLoadStateRenderer][blr] ), управляющих независимыми представлениями (реализации [LoadStatePresentation][lsp]),
актуальными каждый для своего стейта. Представления умеют формировывать отображение стейта на ui, они могут подготавливать View для инфлейта в контейнер, или заполнять адаптер нужными контроллерами, внутри возможна любая логика, необходимая в конкретном проекте.
LoadStateRenderer и набор соответствующих представлений предлагается определять в конкретном проекте, ориентируясь на потребности и дизайн.
LoadStateRenderer не обязательно будет одинаковым на всех экранах приложения, таких сущностей может быть несколько.

Основные классы:

 - [LoadStateRendererInterface][lsri] (бывший ```PlaceHolderViewInterface```) - интерфейс, который обязует реализации уметь обрабатывать состояние экрана
 - [LoadStatePresentation][lsp] - интерфейс, содержащий логику для показа и скрытия определенного стейта
 - [BaseLoadStateRenderer][blr] - базовый класс, хранящий для каждого стейта своё представление, а также список лямбд, которые необходимо выполнить при переходе в новый стейт.
    Содержит группы методов:
     - **forStates()**, принимает список стейтов, для которых стоит выполнить лямбду run(), при рендере остальных стейтов выполнит elseRun()
         ```kotlin
         fun forStates(
            loadStates: List<Class<out LoadStateInterface>>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null): BaseLoadStateRenderer {

        }
        ```
     - **doWithCheck()**, принимает список стейтов, для которых стоит выполнить лямбду runWithCheck(),
        в которую передается наличие текущего стейта в переданном списке loadStates
        ```kotlin
        fun doWithCheck(loadStates: List<Class<out LoadStateInterface>>,
                        action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

                    }
        ```
        удобен для методов типа ```check -> btn.enable = check```
      - **setViewsVisibleFor()**, **setViewsInvisibleFor()**, **setViewsGoneFor()**, позволяют менять видимость вью для определенных стейтов
      - Методы этого класса написаны в стиле [Fluent Interface](https://ru.wikipedia.org/wiki/Fluent_interface), для возможности цепочечного вызова.

## Изменения в модуле core-mvp
  - Enum ```LoadState``` заменен на interface [LoadStateInterface][lsi], любой класс, реализующий этот интерфейс, может выступать в качестве LoadState, он может содержать данные для рендера своего стейта
  - В классах [BaseLdsActivityView][blav] и [BaseLdsFragmentView][blfv]  метод
```PlaceHolderViewInterface getPlaceHolderView() ```
заменен на более обобщенный 
```LoadStateRendererInterface createLoadStateRenderer()```
  - Добавлен пакет ***loadstate.renderer***, в который вошли классы:
    - [LoadStateRendererInterface][lsri] (бывший ```PlaceHolderViewInterface```),
    - [LoadStatePresentation][lsp]
    - [UnknownLoadStateException][ulse] - ошибка, которая может быть вызвана передачей для рендера неизвестного LoadState в [BaseLoadStateRenderer][blr] или в его потомков
    - [BaseLoadStateRenderer][blr]
# Изменения в других модулях
 - **mvp-widget** ```BaseLdsRenderableView``` вместо ```PlaceHolderViewInterface``` теперь обязывает класс реализовывать поле типа ```LoadStateRendererInterface```
 - **custom-view**  в [StandardPlaceHolderView][splhv] enum вложенного класса ```PlaceholderStater``` переименован в ```StandardLoadState```, чтобы не путать разработчиков при импорте своего класса ```LoadState```
 
## Миграция на новый подход с сохранением рендера LoadState через PlaceHolderView
Если вы хотите сохранить обращение к LoadState в стиле enum в своём проекте:
  - Создайте файл LoadState.kt в своем проекте, в нем укажите набор классов, отражающих LoadStates, используемые в проекте, переопредилите в них equals() следующим образом:
```kotlin
class NoneLoadState : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is NoneLoadState
    }
}

class ErrorLoadState : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is ErrorLoadState
    }
}

class EmptyLoadState : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is EmptyLoadState
    }
}

class NotFoundLoadState : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is NotFoundLoadState
    }
}

class MainLoadingState : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is MainLoadingState
    }
}

class TransparentLoadingState : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is TransparentLoadingState
    }
}

class NoInternetLoadState : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is NoInternetLoadState
    }
}
```
  - Создайте объект с полями, имитирующими старое именование
```kotlin
object LoadState {
    val NONE = NoneLoadState()
    val MAIN_LOADING = MainLoadingState()
    val TRANSPARENT_LOADING = TransparentLoadingState()
    val ERROR = ErrorLoadState()
    val EMPTY = EmptyLoadState()
    val NOT_FOUND = NotFoundLoadState()
    val NO_INTERNET = NoInternetLoadState()
} 
```
- Класс PlaceHolderView, должен реализовывать интерфейс LoadStateRendererInterface и принимать LoadStateInterface в методе render
 ```kotlin
 class PlaceHolderView(context: Context, attributeSet: AttributeSet
) : StandardPlaceHolderView(context, attributeSet), LoadStateRendererInterface {
        override fun render(loadState: LoadStateInterface) {...}
    
}
 ```
  - С помощью комбинации 
Ctrl+Shift+R on Windows and Linux
Cmd+Shift+R on Mac OS X
Замените по всему проекту
   - loadState: LoadState
на
loadState: LoadStateInterface

  - import ru.surfstudio.android.core.mvp.model.state.LoadState
на 
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface.

- getPlaceHolderView()
   на 
getLoadStateRenderer()

- далее придется пройтись по проекту и добавить импорт вашего класса LoadState.kt там, где раньше было обращение к enum, если включен автоимпорт, он может сделать это за вас

[lsscm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsScreenModel.java
[lsi]: ../src/main/java/ru/surfstudio/android/core/mvp/loadstate/LoadStateInterface.java
[blav]: ../src/main/java/ru/surfstudio/android/core/mvp/activity/BaseLdsActivityView.java
[blfv]: ../src/main/java/ru/surfstudio/android/core/mvp/fragment/BaseLdsFragmentView.java
[lsri]: ../src/main/java/ru/surfstudio/android/core/mvp/loadstate/LoadStateRendererInterface.java
[lsp]: ../src/main/java/ru/surfstudio/android/core/mvp/loadstate/LoadStatePresentation.kt
[ulse]: ../src/main/java/ru/surfstudio/android/core/mvp/loadstate/UnknownLoadStateException.kt
[blr]: ../src/main/java/ru/surfstudio/android/core/mvp/loadstate/BaseLoadStateRenderer.kt
[splhv]: ../../../custom-view/lib-custom-view/src/main/java/ru/surfstudio/android/custom/view/placeholder/StandardPlaceHolderView.kt