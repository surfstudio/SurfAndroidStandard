package ru.surfstudio.android.core.app.interactor.common;


import android.content.SharedPreferences;
import android.text.TextUtils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ru.surfstudio.android.core.util.CollectionUtils;
import ru.surfstudio.android.core.util.SettingsUtil;

/**
 * Хранилище для сохранения набора id
 */
public abstract class BaseLocalIntIdsCache {

    private static final String SEPARATOR = ",";

    /**
     * @return куда писать данные
     */
    protected abstract SharedPreferences sharedPreferences();

    /**
     * @return ключ под которым будут храниться данные
     */
    protected abstract String storageKey();

    /**
     * Добавить id
     */
    public void addId(Integer id) {
        addIds(CollectionUtils.newSet(Collections.singletonList(id)));
    }

    /**
     * Удалить id
     */
    public void removeId(Integer id) {
        removeIds(CollectionUtils.newSet(Collections.singletonList(id)));
    }

    /**
     * Добавить id-шники
     */
    public void addIds(Set<Integer> integers) {
        Set<Integer> ids = getIds();
        ids.addAll(integers);
        replaceIds(ids);
    }

    /**
     * Удалить id шники
     */
    public void removeIds(Set<Integer> integers) {
        Set<Integer> ids = getIds();
        ids.removeAll(integers);
        replaceIds(ids);
    }

    /**
     * @return сохраненные id
     */
    public Set<Integer> getIds() {
        String rawIds = SettingsUtil.getString(sharedPreferences(), storageKey());
        return TextUtils.isEmpty(rawIds)
                ? new HashSet<>(0)
                : Stream.of(rawIds.split(SEPARATOR)).map(Integer::valueOf).collect(Collectors.toSet());
    }

    /**
     * Очистить сохраненные id
     */
    public void clearSavedIds() {
        SettingsUtil.putString(sharedPreferences(), storageKey(), SettingsUtil.EMPTY_STRING_SETTING);
    }

    private void replaceIds(Set<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        Stream.of(ids).forEach(value -> sb.append(sb.length() > 0 ? SEPARATOR : "").append(value));
        SettingsUtil.putString(sharedPreferences(), storageKey(), sb.toString());
    }
}
