package com.surf.myapplication.interactor.auth;


import com.surf.myapplication.interactor.auth.token.TokenStorage;
import com.surf.myapplication.interactor.profile.UserRepository;

import javax.inject.Inject;

public class SessionChangedInteractor {

    private UserRepository userRepository;
    private TokenStorage tokenStorage;

    @Inject
    public SessionChangedInteractor(UserRepository userRepository,
                                    TokenStorage tokenStorage) {
        this.userRepository = userRepository;
        this.tokenStorage = tokenStorage;
    }


    public void onLogin(UserObj userObj) {
        userRepository.cacheProfile(userObj.transform());
        tokenStorage.saveToken(userObj.getToken());
    }

    public void onLogout(LogoutResponse logoutResponse) {
        userRepository.clearProfileCache();
        tokenStorage.saveToken(logoutResponse.getToken());
    }

    public void onTokenChanged(TokenResponse tokenResponse) {
        tokenStorage.saveToken(tokenResponse.getToken());
    }

    /**
     * действия для принудительной очистки данных о пользователе
     */
    public void onForceLogout() {
        tokenStorage.saveToken(null);
        userRepository.clearProfileCache();
        //todo Maks Tuev add logic for force logout
    }
}
