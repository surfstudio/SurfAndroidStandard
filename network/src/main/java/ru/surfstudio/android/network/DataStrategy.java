package ru.surfstudio.android.core.app.interactor.common;

/**
 * что сделать в первую очередь,
 * вытянуть данные из кеша или попытаться загрузить свежие
 */
public enum DataPriority {
    CACHE,
    SERVER,
    ONLY_ACTUAL,
    AUTO
}