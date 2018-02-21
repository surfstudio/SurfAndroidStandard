package ru.surfstudio.standard.ui.base.dagger.widget

import dagger.Module
import ru.surfstudio.android.mvp.dialog.dagger.DialogNavigatorForWidgetModule
import ru.surfstudio.android.mvp.widget.dagger.CoreWidgetScreenModule
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = [
    CoreWidgetScreenModule::class,
    ErrorHandlerModule::class,
    DialogNavigatorForWidgetModule::class])
class WidgetScreenModule