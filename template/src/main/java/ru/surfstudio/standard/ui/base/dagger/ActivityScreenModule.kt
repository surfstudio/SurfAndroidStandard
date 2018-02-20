package ru.surfstudio.standard.ui.base.dagger

import dagger.Module
import ru.surfstudio.android.core.mvp.dagger.CoreActivityScreenModule
import ru.surfstudio.android.mvp.dialog.dagger.DialogNavigatorForActivityModule
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = arrayOf(
        CoreActivityScreenModule::class,
        ErrorHandlerModule::class,
        DialogNavigatorForActivityModule::class))
class ActivityScreenModule