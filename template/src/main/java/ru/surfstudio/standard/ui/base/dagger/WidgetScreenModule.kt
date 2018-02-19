package ru.surfstudio.standard.ui.base.dagger

import dagger.Module
import ru.surfstudio.android.mvp.dialog.dagger.DialogNavigatorForWidgetModule
import ru.surfstudio.android.mvp.widget.dagger.CoreWidgetScreenModule
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = arrayOf(
        CoreWidgetScreenModule::class,
        ErrorHandlerModule::class,
        DialogNavigatorForWidgetModule::class))
class WidgetScreenModule