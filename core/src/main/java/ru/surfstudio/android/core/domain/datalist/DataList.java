package ru.surfstudio.android.core.domain.datalist;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * List для работы с пагинацией
 * Имеет лимит и смещение
 * Можно сливать с другим DataList
 *
 * @param <T> Item
 */
public class DataList<T> implements List<T>, Serializable {

    private int limit;
    private int skip;
    private int totalCount;

    private ArrayList<T> data;

    public interface MapFunc<R, T> {
        R call(T item);
    }

    public static <T> DataList<T> empty() {
        return new DataList<>(new ArrayList<>(), 0, 0, 0);
    }

    public DataList(Collection<T> data, int limit, int skip, int totalCount) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
        this.limit = limit;
        this.skip = skip;
        this.totalCount = totalCount;
    }

    /**
     * Слияние двух DataList
     *
     * @param data DataList для слияния с текущим
     * @return текущий экземпляр
     */
    public DataList<T> merge(DataList<T> data) {
        boolean reverse = data.skip < this.skip;
        ArrayList<T> merged = tryMerge(reverse ? data : this, reverse ? this : data);
        if (merged == null) {
            //Отрезки данных не совпадают, слияние не возможно
            throw new IllegalArgumentException("incorrect data range");
        }
        this.data.clear();
        this.data.addAll(merged);
        if (this.skip < data.skip) {
            this.limit = data.skip + data.limit - this.skip;
        } else if (this.skip == data.skip) {
            this.limit = data.limit;
        } else {
            this.skip = data.skip;
        }

        this.totalCount = data.totalCount;
        return this;
    }

    public <R> DataList<R> transform(MapFunc<R, T> mapFunc) {
        List<R> resultData = new ArrayList<>();
        for (T item : this) {
            resultData.add(mapFunc.call(item));
        }

        return new DataList<>(resultData, skip, limit, totalCount);
    }

    /**
     * возвращает значение skip c которого нужно начать чтобы подгрузить слкдующий блок данных
     */
    public int getNextSkip() {
        return limit + skip;
    }

    public int getLimit() {
        return limit;
    }

    public int getSkip() {
        return skip;
    }

    public int getTotalCount() {
        return totalCount;
    }

    /**
     * Проверка возможности дозагрузки данных
     *
     * @return
     */
    public boolean canGetMore() {
        return totalCount > data.size();
    }

    @Nullable
    private ArrayList<T> tryMerge(DataList<T> to, DataList<T> from) {
        if ((to.skip + to.limit) >= from.skip) {
            return merge(to.data, from.data, from.skip - to.skip);
        }

        return null;
    }

    private ArrayList<T> merge(ArrayList<T> to, ArrayList<T> from, int start) {
        ArrayList<T> result = new ArrayList<>();
        result.addAll(start < to.size() ? to.subList(0, start) : to);
        result.addAll(from);
        return result;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] a) {
        return data.toArray(a);
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return data.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.data.clear();
        limit = 0;
        skip = 0;
    }

    @Override
    public T get(int index) {
        return data.get(index);
    }

    @Override
    public T set(int index, T element) {
        return data.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        return data.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator() {
        return data.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return data.listIterator(index);
    }

    @NonNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataList)) return false;

        DataList<?> dataList = (DataList<?>) o;

        return limit == dataList.limit && skip == dataList.skip &&
                (data != null ? data.equals(dataList.data) : dataList.data == null);

    }

    @Override
    public int hashCode() {
        int result = limit;
        result = 31 * result + skip;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataList" +
                "{limit=" + limit +
                ", skip=" + skip +
                ", data=" + data +
                '}';
    }
}
