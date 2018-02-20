package ru.surfstudio.standard.ui.base.dagger

import dagger.Module
import ru.surfstudio.android.core.mvp.dagger.CoreFragmentScreenModule
import ru.surfstudio.android.mvp.dialog.dagger.DialogNavigatorForFragmentModule
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule

@Module(includes = arrayOf(
        CoreFragmentScreenModule::class,
        ErrorHandlerModule::class,
        DialogNavigatorForFragmentModule::class))
class FragmentScreenModule
