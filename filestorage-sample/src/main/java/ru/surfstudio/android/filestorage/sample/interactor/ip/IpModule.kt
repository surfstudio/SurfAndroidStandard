package ru.surfstudio.android.filestorage.sample.interactor.ip

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.ObjectConverter
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import ru.surfstudio.android.filestorage.sample.interactor.ip.network.IpApi

@Module
class IpModule {

    @Provides
    @PerApplication
    internal fun provideIpApi(retrofit: Retrofit): IpApi = retrofit.create(IpApi::class.java)

    @Provides
    @PerApplication
    internal fun provideIpObjectConverter() = object : ObjectConverter<Ip> {

        private val gsonBuilder by lazy { GsonBuilder().create() }

        override fun encode(value: Ip?): ByteArray = gsonBuilder.toJson(value).toByteArray()

        override fun decode(rawValue: ByteArray): Ip = gsonBuilder.fromJson(String(rawValue), Ip::class.java)
    }
}