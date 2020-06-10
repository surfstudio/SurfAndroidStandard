# StandardDialog
Поставляет простой кастомизируемый да/нет диалог

# Использование
Презентер экрана, на котором планируется использовать диалог, должен реализовывать интерфейс
StandardDialogPresenter.

Пример: 

    @PerScreen
    internal class MainPresenter ... :StandardDialogPresenter {

        override fun positiveBtnAction(dialogTag: String) {
            if (dialogTag == "tagSimple") view.toast("ok")
        }

        override fun negativeBtnAction(dialogTag: String) {
            if (dialogTag == "tagSimple") view.toast("cancel")
        }
    }

Если на экране вызывается несколько разных StandardDialog, dialogTag помогает разобраться, от которого диалога пришел ответ.

Конфигуратор экрана должен предоставлять StandardDialogPresenter:

    @Module
    internal class MainScreenModule(route: MainActivityRoute) :
            CustomScreenModule<MainActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideStandardDialogPresenter(presenter: MainPresenter): StandardDialogPresenter {
            return presenter
        }
    }
    
Конфигуратор должен реализовывать интерфейс StandardDialogComponent:
    
        @PerScreen
        @Component(dependencies = [ActivityComponent::class], modules = [ActivityScreenModule::class, MainScreenModule::class])
        internal interface MainScreenComponent : ScreenComponent<MainActivityView>, StandardDialogComponent

# Примеры создания диалога

Через ресурсы:

    dialogNavigator.show(StandardDialogRoute(
                titleRes= R.string.title,
                messageRes = R.string.message,
                possitiveBtnTextRes = R.string.possitive,
                negativeBtnTextRes = R.string.negative,
                isCancelable = true,
                dialogTag = "tagSimple"))
                
Через строки:
 
    dialogNavigator.show(StandardDialogRoute(
                 title= "title",
                 message = "message",
                 possitiveBtnText = "ok",
                 negativeBtnText= "no",
                 isCancelable = true,
                 dialogTag = "tagSimple"))
                 
 Комбинированный вариант:
  
    dialogNavigator.show(StandardDialogRoute(
                  titleRes= R.string.title,
                  message = stringsProvider.getString(R.string.message, 21),
                  possitiveBtnTextRes = R.string.possitive,
                  negativeBtnTextRes = R.string.negative,
                  isCancelable = true,
                  dialogTag = "tagSimple"))
                  
[Пример использования](../standard-dialog-sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:standard-dialog:X.X.X"
```