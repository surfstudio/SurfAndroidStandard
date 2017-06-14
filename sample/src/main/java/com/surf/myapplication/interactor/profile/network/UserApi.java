package com.surf.myapplication.interactor.profile.network;

import retrofit2.http.GET;
import retrofit2.http.Header;
import ru.labirint.android.interactor.common.network.response.UserResponse;
import rx.Observable;

import static ru.labirint.android.interactor.common.network.ServerConstants.HEADER_QUERY_MODE;
import static ru.labirint.android.interactor.common.network.ServerConstants.QueryMode;
import static ru.labirint.android.interactor.common.network.ServerUrls.*;
import static ru.labirint.android.interactor.common.network.ServerUrls.USER_URL;

public interface UserApi {

    @GET
    Observable<UserResponse> getProfile(@QueryMode int queryMode);
}
