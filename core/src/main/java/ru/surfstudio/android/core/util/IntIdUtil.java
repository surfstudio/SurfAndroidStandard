package ru.surfstudio.android.core.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Позволяет из коллекции с элементами, имплементирующими IntId, получить
 * элемент по совпадающему значению
 */

public class IntIdUtil {

    private IntIdUtil() {
        //do nothing
    }

    public static <T extends IntId> T getByValue(T[] values, int value) {
        for (T t : values) {
            if (t.id() == value) {
                return t;
            }
        }
        throw new IllegalStateException(String.format("Not found id: %s in values: %s with ids %s",
                value, Arrays.toString(values), ids(values)));
    }

    private static <T extends IntId> List<Integer> ids(T[] values) {
        List<Integer> result = new ArrayList<>(values.length);
        for (T value : values) {
            result.add(value.id());
        }
        return result;
    }
}
