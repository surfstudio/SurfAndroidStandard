package ru.surfstudio.android.core.app.interactor.common.network;


/**
 * Дескриптор для включения или отключения фильтра при запросе
 */
public class FilterIncludeDescriptor {
    private static final int INCLUDE_FILTER = 1;
    private static final int EXCLUDE_FILTER = 0;

    public static int setFilterEnable(boolean enable) {
        return enable ? INCLUDE_FILTER : EXCLUDE_FILTER;
    }
}