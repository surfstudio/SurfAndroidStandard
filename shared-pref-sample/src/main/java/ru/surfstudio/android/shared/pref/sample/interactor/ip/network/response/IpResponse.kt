package ru.surfstudio.android.shared.pref.sample.interactor.ip.network.response

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.shared.pref.sample.domain.ip.Ip
import ru.surfstudio.android.network.Transformable

data class IpResponse(@SerializedName("ip") val ip: String) : Transformable<Ip> {
    override fun transform() = Ip(ip)
}