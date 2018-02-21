package ru.surfstudio.standard.ui.base.dagger.widget

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigatorForWidget
import ru.surfstudio.android.mvp.widget.dagger.CoreWidgetScreenModule
import ru.surfstudio.android.mvp.widget.provider.WidgetProvider
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = [
    CoreWidgetScreenModule::class,
    ErrorHandlerModule::class])
class WidgetScreenModule {
    @Provides
    @PerScreen
    internal fun provideDialogNavigator(activityProvider: ActivityProvider, widgetProvider: WidgetProvider): DialogNavigator {
        return DialogNavigatorForWidget(activityProvider, widgetProvider)
    }
}