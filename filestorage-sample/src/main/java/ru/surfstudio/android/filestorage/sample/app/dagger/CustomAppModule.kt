package ru.surfstudio.android.filestorage.sample.app.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.Encryptor
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProviderImpl

@Module
class CustomAppModule(private val coreApp: CoreApp) {

    @PerApplication
    @Provides
    internal fun provideActiveActivityHolder(): ActiveActivityHolder {
        return coreApp.activeActivityHolder
    }

    @PerApplication
    @Provides
    internal fun provideContext(): Context {
        return coreApp
    }

    @PerApplication
    @Provides
    internal fun provideStringsProvider(context: Context): StringsProvider {
        return StringsProvider(context)
    }

    @PerApplication
    @Provides
    internal fun provideGlobalNavigator(context: Context,
                                        activityHolder: ActiveActivityHolder): GlobalNavigator {
        return GlobalNavigator(context, activityHolder)
    }

    @Provides
    @PerApplication
    internal fun provideSchedulerProvider(): SchedulersProvider {
        return SchedulersProviderImpl()
    }

    //todo добавить механизм шифрования и дешифрования
    @Provides
    @PerApplication
    internal fun provideEncryptor() = object : Encryptor {

        override fun encrypt(decryptedBytes: ByteArray): ByteArray = decryptedBytes

        override fun decrypt(rawBytes: ByteArray): ByteArray = rawBytes
    }
}