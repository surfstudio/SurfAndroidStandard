package ru.surfstudio.android.filestorage.sample.interactor.ip.network.response

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import ru.surfstudio.android.network.Transformable

data class IpResponse(@SerializedName("ip") val ip: String) : Transformable<Ip> {
    override fun transform() = Ip(ip)
}