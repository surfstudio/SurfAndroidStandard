package ru.surfstudio.android.filestorage;

public class CacheConstant {
    /**
     * Имя в аннотации {@link javax.inject.Named} для пути к директории с наиболее важным кешем
     */
    public static final String INTERNAL_CACHE_DIR_DAGGER_NAME = "internalCacheDir";
    /**
     * Имя в аннотации {@link javax.inject.Named} для пути к директории с неважным кешем
     */
    public static final String EXTERNAL_CACHE_DIR_DAGGER_NAME = "externalCacheDir";
}
