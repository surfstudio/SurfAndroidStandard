package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.rxbus.RxBus

/**
 * Модуль для dagger SpecialActivity Component
 * поставляет ряд сущностей, например навигаторы, причем они находятся в @PerActivity scope
 * и пробрасываются в опредеоенную активити.
 * Эти обьекты могут также использоваться внутри дополнительных обектов со специфической логикой,
 * принадлежащих скоупу @PerScreen
 */

@Module
class LoginActivityModule {

    @Provides
    @PerActivity
    internal fun provideRxBus(): RxBus {
        return RxBus()
    }
}
