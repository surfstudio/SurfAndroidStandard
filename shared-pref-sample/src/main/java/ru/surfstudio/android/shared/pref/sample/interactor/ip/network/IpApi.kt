package ru.surfstudio.android.shared.pref.sample.interactor.ip.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.surfstudio.android.shared.pref.sample.domain.common.QueryConstants.QUERY_FORMAT_NAME
import ru.surfstudio.android.shared.pref.sample.domain.common.QueryConstants.QUERY_FORMAT_VALUE
import ru.surfstudio.android.shared.pref.sample.interactor.common.network.ServerUrls.GET_IP_URL
import ru.surfstudio.android.shared.pref.sample.interactor.ip.network.response.IpResponse

interface IpApi {

    @GET(GET_IP_URL)
    fun getIp(@Query(QUERY_FORMAT_NAME) format: String = QUERY_FORMAT_VALUE): Observable<IpResponse>
}