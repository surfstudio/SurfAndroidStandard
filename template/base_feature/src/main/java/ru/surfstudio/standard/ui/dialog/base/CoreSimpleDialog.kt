package ru.surfstudio.standard.ui.dialog.base

import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.standard.application.app.di.AppComponent
import ru.surfstudio.standard.application.app.di.AppInjector

/**
 * Базовый простой диалог с поддержкой возвращения результата и навигации.
 *
 * Имеет доступ к Dagger-компоненту приложения [AppComponent] через метод [getAppComponent]
 * Может вернуть результат своей работы через метод [NavigationCommandExecutor.execute]
 * и передачу в него команды [EmitScreenResult].
 */
interface CoreSimpleDialog {

    fun getAppComponent(): AppComponent = AppInjector.appComponent

    fun getCommandExecutor(): NavigationCommandExecutor = getAppComponent().commandExecutor()
}
