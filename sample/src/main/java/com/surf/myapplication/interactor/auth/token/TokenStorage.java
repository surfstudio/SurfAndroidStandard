package com.surf.myapplication.interactor.auth.token;

import com.agna.ferro.mvp.component.scope.PerApplication;

import javax.inject.Inject;

@PerApplication
public class TokenStorage {

    private static final String CACHE_TOKEN_KEY = "token_key";
    private TokenCache tokenCache;

    @Inject
    public TokenStorage(final TokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    public String getToken() {
        final TokenProto proto = tokenCache.get(CACHE_TOKEN_KEY);
        return proto != null ? proto.token : "";
    }

    public void saveToken(final String token) {
        tokenCache.put(CACHE_TOKEN_KEY, new TokenProto.Builder()
                .token(token)
                .build());
    }
}
