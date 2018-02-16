package ru.surfstudio.android.filestorage;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.dagger.scope.PerApplication;
import ru.surfstudio.android.utilktx.util.java.CollectionUtils;

@Module
public class CacheModule {

    @Provides
    @PerApplication
    @Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME)
    public String provideInternalCacheDir(final Context context) {
        return ContextCompat.getNoBackupFilesDir(context).getAbsolutePath();
    }

    @Provides
    @PerApplication
    @Named(CacheConstant.EXTERNAL_CACHE_DIR_DAGGER_NAME)
    public String provideExternalCacheDir(final Context context) {
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
        // могут возвращаться null элементы, убираем их
        List<File> filtered = CollectionUtils.filter(Arrays.asList(externalFilesDirs), file -> file != null);
        // берем последний из списка
        File result = CollectionUtils.last(filtered);
        // если подходящего элемента не оказалось, берем директорию внутреннего кэша
        return result != null ? result.getAbsolutePath() : provideInternalCacheDir(context);
    }
}
