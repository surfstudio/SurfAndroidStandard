package ru.surfstudio.android.core.app.interactor.common.cache.file.naming;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * создает имя(файла, директории) из сырой строки
 */
public class SimpleNamingProcessor implements NamingProcessor {
    public static final String DOT = ".";

    @Nullable
    @Override
    public String getNameFrom(@NotNull String rawName) {
        return rawName.replaceAll("[/?=&]", DOT);
    }
}
