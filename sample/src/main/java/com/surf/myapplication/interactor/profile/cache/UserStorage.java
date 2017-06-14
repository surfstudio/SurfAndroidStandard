package com.surf.myapplication.interactor.profile.cache;

import com.agna.ferro.mvp.component.scope.PerApplication;

import javax.inject.Inject;

import ru.labirint.android.domain.user.User;

/**
 * хранилище профиля пользователя
 */
@PerApplication
public class UserStorage {

    private static final String CURRENT_USER = "current_user";

    private final ProfileCache cache;
    private final ProfileCacheTransformer transformer;

    @Inject
    public UserStorage(final ProfileCache cache, final ProfileCacheTransformer transformer) {
        this.cache = cache;
        this.transformer = transformer;
    }

    /**
     * сохраняет профиль в кеш
     */
    public void saveUser(User user) {
        final ProfileProto proto = transformer.buildProto(user);
        cache.put(CURRENT_USER, proto);
    }

    /**
     * @return профиль из кеша
     */
    public User getUser() {
        final ProfileProto proto = cache.get(CURRENT_USER);
        return transformer.buildUser(proto);
    }

    /**
     * удаляет кеш профиля
     */
    public void clearUser() {
        cache.clear();
    }
}
