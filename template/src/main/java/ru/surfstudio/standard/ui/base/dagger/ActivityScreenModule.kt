package ru.surfstudio.standard.ui.base.dagger

import dagger.Module
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = arrayOf(CoreActivityScreenModule::class, ErrorHandlerModule::class))
class ActivityScreenModule