# LoadState Concept

## Изменения в модуле core-mvp
  - Enum ```LoasState``` заменен на interface ```LoadStateInterface```, любой класс, реализующий этот интерфейс, может выступать в качестве LoadState, он может содержать данные для рендера своего стейта
  - В классах ```BaseLdsActivityView``` и ```BaseLdsFragmentView```  метод 
```PlaceHolderViewInterface getPlaceHolderView() ```
заменен на более обобщенный 
```LoadStateRendererInterface createLoadStateRenderer()```
  - Добавлен пакет ***loadstate.renderer***, в который вошли классы:
    - ```LoadStateRendererInterface``` (бывший ```PlaceHolderViewInterface```),
    - ``` LoadStatePresentation``` - интерфейс, содержащий логику для показа и скрытия определенного стейта
    - ```UnknownLoadStateException``` - ошибка, которая может быть вызвана передачей для рендера неизвестного LoadState в ```BaseLoadStateRenderer``` или в его потомков
    - ```BaseLoadStateRenderer``` - базовый класс, хранящий для каждого стейта своё представление, а также список лямбд, которые необходимо выполнить при переходе в новый стейт.
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
                    runWithCheck: (check: Boolean) -> Unit): BaseLoadStateRenderer {
                    
                    }
        ```
        удобен для методов типа ```check -> btn.enable = check```
        - **setViewsVisibleFor()**, **setViewsInvisibleFor()**, **setViewsGoneFor()**, позволяют менять видимость вью для определенных стейтов
        - Методы этого класса написаны в стиле [Fluent Interface](https://ru.wikipedia.org/wiki/Fluent_interface), для возможности цепочечного вызова.
## Изменения в других модулях
 - **recycler-extension:** ```ItemListExt.kt``` переехал из **ru.surfstudio.android.recycler.extension.sticky** в **ru.surfstudio.android.recycler.extension** , В него добавлен метод для добавления n-штук ```NoDataItemController```, который полезен при добавление пачки заглушек
 - **mvp-widget** ```BaseLdsRenderableView``` вместо ```PlaceHolderViewInterface``` теперь обязывает класс реализовывать поле типа ```LoadStateRendererInterface```
 - **custom-view**  в ```StandardPlaceHolderView``` enum вложенного класса ```PlaceholderStater``` переименован в ```StandardLoadState```, чтобы не путать разработчиков при импорте своего класса ```LoadState```
 
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
        override fun render(loadState: LoadStateInterface) {}
    
}
 ```
  - С помощью комбинации 
Ctrl+Shift+R on Windows and Linux/Ubuntu
Cmd+Shift+R on Mac OS X
Замените по всему проекту
   - loadState: LoadState
на
loadState: LoadStateInterface

  - import ru.surfstudio.android.core.mvp.model.state.LoadState
на 
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface

- getLoadStateRenderer()
   на 
createLoadStateRenderer()

- далее придется пройтись по проекту и добавить импорт вашего класса LoadState.kt там, где раньше было обращение к enum, если включен автоимпорт, он может сделать это за вас
