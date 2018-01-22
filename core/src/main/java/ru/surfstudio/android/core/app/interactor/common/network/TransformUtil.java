package ru.surfstudio.android.core.app.interactor.common.network;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;

import ru.surfstudio.android.core.util.CollectionUtils;
import ru.surfstudio.android.core.util.Mapper;
import ru.surfstudio.android.core.util.Transformable;


/**
 * содержит методы для трансформации объектов Domain слоя в данные для сервера и наоборот.
 * Упрощает работу с {@link Transformable}
 */
public final class TransformUtil {

    private TransformUtil() {
        throw new IllegalStateException("no instance allowed");
    }

    public static <T, E extends Transformable<T>> T transform(@Nullable E object) {
        return object != null ? object.transform() : null;
    }

    public static <T, E extends Transformable<T>> List<T> transformCollection(Collection<E> src) {
        return CollectionUtils.mapEmptyIfNull(src, Transformable::transform);
    }

    public static <T, E> List<T> transformCollection(Collection<E> src, final Mapper<E, T> mapper) {
        return CollectionUtils.mapEmptyIfNull(src, mapper);
    }
}
