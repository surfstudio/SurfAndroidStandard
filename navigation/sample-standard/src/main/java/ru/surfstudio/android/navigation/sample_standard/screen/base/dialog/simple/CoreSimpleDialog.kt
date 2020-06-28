package ru.surfstudio.android.navigation.sample_standard.screen.base.dialog.simple

import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.sample_standard.App
import ru.surfstudio.android.navigation.sample_standard.di.AppComponent

/**
 * Базовый простой диалог с поддержкой возвращения результата и навигации.
 *
 * Имеет доступ к Dagger-компоненту приложения [AppComponent] через метод [getAppComponent]
 * Может вернуть результат своей работы через метод [NavigationCommandExecutor.execute]
 * и передачу в него команды [EmitScreenResult].
 */
abstract class CoreSimpleDialog : DialogFragment() {

    protected fun getAppComponent(): AppComponent {
        val app = requireActivity().baseContext as App
        return app.appComponent
    }

    protected fun getCommandExecutor(): NavigationCommandExecutor = getAppComponent().commandExecutor()
}
