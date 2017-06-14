package com.surf.myapplication.interactor.profile;

import android.support.annotation.WorkerThread;

import com.surf.myapplication.interactor.common.BaseRepository;
import com.surf.myapplication.interactor.auth.token.TokenStorage;
import com.surf.myapplication.interactor.profile.cache.UserStorage;
import com.surf.myapplication.interactor.profile.network.UserApi;

import javax.inject.Inject;

import io.reactivex.Observable;

public class UserRepository extends BaseRepository {

    private UserApi userApi;
    private UserStorage userStorage;
    private TokenStorage tokenStorage;

    @Inject
    public UserRepository(UserApi userApi,
                          UserStorage userStorage,
                          TokenStorage tokenStorage) {
        this.userApi = userApi;
        this.userStorage = userStorage;
        this.tokenStorage = tokenStorage;
    }

    /**
     * сохраняет профиль в кеш
     */
    @WorkerThread
    public void cacheProfile(User user) {
        userStorage.saveUser(user);
    }

    /**
     * очищает кеш профиля
     */
    @WorkerThread
    public void clearProfileCache() {
        userStorage.clearUser();
    }

    /**
     * @return профиль пользователя
     */
    public Observable<User> getUser() {
        return hybridQuery(
                Observable.fromCallable(() -> userStorage.getUser()),
                queryMode -> userApi.getProfile(queryMode)
                        .doOnNext(resp -> onUserChanged(resp.getUserObj()))
                        .map(resp -> resp.getUserObj().transform()));

    }

    /**
     * показывает, есть ли в системе авторизованный пользователь
     * @return
     */
    public boolean isUserLoggedIn() {
        return userStorage.getUser() != null;
    }

    /**
     * содержит действия, которые нужно выполнить при получении обновленного профиля
     */
    @WorkerThread
    private void onUserChanged(UserObj userObj) {
        cacheProfile(userObj.transform());
        tokenStorage.saveToken(userObj.getToken());
    }


}
