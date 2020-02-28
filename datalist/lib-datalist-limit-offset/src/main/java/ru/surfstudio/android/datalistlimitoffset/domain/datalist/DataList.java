/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.datalistlimitoffset.domain.datalist;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * List для работы с пагинацией
 * Механизм limit-offset
 * Можно сливать с другим DataList
 *
 * @param <T> Item
 */
public class DataList<T> implements List<T>, Serializable {

    //количество элементов в списке
    private int limit;
    //сдвиг относительно первого элемента
    private int offset;
    //максимально возможное количество эелементов списка
    private int totalCount;

    private ArrayList<T> data;

    public interface MapFunc<R, T> {
        R call(T item);
    }

    public static <T> DataList<T> empty() {
        return new DataList(new ArrayList<>(), 0, 0, 0);
    }

    public static <T> DataList<T> emptyWithTotal(int totalCount) {
        return new DataList(new ArrayList<>(), 0, 0, totalCount);
    }

    public DataList(Collection<T> data, int limit, int offset) {
        this(data, limit, offset, 0);
    }

    public DataList(Collection<T> data, int limit, int offset, int totalCount) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
        this.limit = limit;
        this.offset = offset;
        this.totalCount = totalCount;
    }

    /**
     * Слияние двух DataList
     *
     * @param data DataList для слияния с текущим
     * @return текущий экземпляр
     */
    public DataList<T> merge(DataList<T> data) {
        boolean reverse = data.offset < this.offset;
        ArrayList<T> merged = tryMerge(reverse ? data : this, reverse ? this : data);
        if (merged == null) {
            //Отрезки данных не совпадают, слияние не возможно
            throw new IncompatibleRangesException("incorrect data range");
        }
        this.data.clear();
        this.data.addAll(merged);
        if (this.offset < data.offset) { //загрузка вниз, как обычно
            this.limit = data.offset + data.limit - this.offset;
        } else if (this.offset == data.offset) { //коллизия?
            this.limit = data.limit;
        } else { // загрузка вверх
            this.offset = data.offset;
            this.limit = size();
        }

        this.totalCount = data.totalCount;
        return this;
    }

    /**
     * Слияние двух DataList с удалением дублируемых элементов
     * При удалении остаются актуальные (последние присланные сервером) элементы
     *
     * @param data              DataList для слияния с текущим
     * @param distinctPredicate предикат, по которому происходит удаление дублируемых элементов
     * @return текущий экземпляр
     */
    public <R> DataList<T> merge(DataList<T> data, MapFunc<R, T> distinctPredicate) {
        boolean reverse = data.offset < this.offset;
        ArrayList<T> merged = tryMerge(reverse ? data : this, reverse ? this : data);
        if (merged == null) {
            //Отрезки данных не совпадают, слияние не возможно
            throw new IncompatibleRangesException("incorrect data range");
        }

        ArrayList<T> filtered = distinctByLast(merged, distinctPredicate);
        this.data.clear();
        this.data.addAll(filtered);
        if (this.offset < data.offset) { //загрузка вниз, как обычно
            this.limit = data.offset + data.limit - this.offset;
        } else if (this.offset == data.offset) { //коллизия?
            this.limit = data.limit;
        } else { // загрузка вверх
            this.offset = data.offset;
            this.limit = size();
        }

        this.totalCount = data.totalCount;
        return this;
    }

    /**
     * Преобразует dataList одного типа в dataList другого типа
     *
     * @param mapFunc функция преобразования
     * @param <R>     тип данных нового списка
     * @return DataList с элементами типа R
     */
    public <R> DataList<R> transform(MapFunc<R, T> mapFunc) {
        List<R> resultData = new ArrayList<>();
        for (T item : this) {
            resultData.add(mapFunc.call(item));
        }

        return new DataList<>(resultData, limit, offset, totalCount);
    }

    /**
     * возвращает значение offset c которого нужно начать чтобы подгрузить слкдующий блок данных
     */
    public int getNextOffset() {
        return limit + offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
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
        return totalCount > limit + offset;
    }

    @Nullable
    private ArrayList<T> tryMerge(DataList<T> to, DataList<T> from) {
        if ((to.offset + to.limit) >= from.offset) {
            return merge(to.data, from.data, from.offset - to.offset);
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
        offset = 0;
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

        return limit == dataList.limit && offset == dataList.offset &&
                (data != null ? data.equals(dataList.data) : dataList.data == null);

    }

    @Override
    public int hashCode() {
        int result = limit;
        result = 31 * result + offset;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataList" +
                "{limit=" + limit +
                ", offset=" + offset +
                ", data=" + data +
                '}';
    }

    /**
     * Удаление одинаковых элементов из исходного списка
     * Критерий того, что элементы одинаковые, задается параметром distinctPredicate
     *
     * @param source            исходный список
     * @param distinctPredicate критерий того, что элементы одинаковые
     * @return отфильтрованный список без одинаковых элементов
     */
    private <R> ArrayList<T> distinctByLast(ArrayList<T> source, MapFunc<R, T> distinctPredicate) {
        HashMap<R, T> resultSet = new LinkedHashMap<>();

        for (T element : source) {
            R key = distinctPredicate.call(element);
            resultSet.put(key, element);
        }

        return new ArrayList<>(resultSet.values());
    }
}
