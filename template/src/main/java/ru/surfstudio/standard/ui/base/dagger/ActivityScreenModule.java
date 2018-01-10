package ru.surfstudio.standard.ui.base.dagger;

import dagger.Module;
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule;
import ru.surfstudio.standard.ui.base.error.ErrorHandlerModule;

@Module(includes = {CoreActivityScreenModule.class, ErrorHandlerModule.class})
public class ActivityScreenModule {
}