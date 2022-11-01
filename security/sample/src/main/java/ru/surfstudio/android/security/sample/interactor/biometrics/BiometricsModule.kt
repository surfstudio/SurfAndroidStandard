package ru.surfstudio.android.security.sample.interactor.biometrics

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.biometrics.BiometricsService
import ru.surfstudio.android.biometrics.encryptor.BiometricsEncryptorFactory
import ru.surfstudio.android.biometrics.encryptor.BiometricsKeyCreator
import ru.surfstudio.android.biometrics.encryptor.DefaultBiometricsEncryptor
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.security.crypto.KeyEncryptor
import java.security.Key

@Module
class BiometricsModule {

    @Provides
    @PerApplication
    fun provideBiometricsService(
        context: Context,
        schedulersProvider: SchedulersProvider,
        keyCreator: BiometricsKeyCreator,
        factory: BiometricsEncryptorFactory
    ): BiometricsService {
        return BiometricsService(context, schedulersProvider, keyCreator, factory)
    }

    @Provides
    @PerApplication
    fun provideKeyCreator(): BiometricsKeyCreator = BiometricsKeyCreator()

    @Provides
    @PerApplication
    fun provideEncryptorFactory(): BiometricsEncryptorFactory {
        return object : BiometricsEncryptorFactory {
            override fun getEncyptor(key: Key): KeyEncryptor<Key> {
                return DefaultBiometricsEncryptor(key)
            }
        }
    }
}