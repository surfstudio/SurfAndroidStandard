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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Pagination list with limit-offset mechanism.
 * <p>
 * Two DataLists can be combined by calling merge function.
 * This function is the only way to merge pagination data blocks.
 *
 * @param <T> item type
 */
public class DataList<T> implements List<T>, Serializable {

    private int limit;
    private int offset;
    private int totalCount;

    private ArrayList<T> data;

    /**
     * Mapper Function Interface which is used to transform one type to another.
     *
     * @param <R> result type
     * @param <T> source type
     */
    public interface MapFunc<R, T> {
        R call(T item);
    }

    /**
     * Creates empty DataList
     *
     * @param <T> item type
     * @return DataList instance without elements
     */
    public static <T> DataList<T> empty() {
        return new DataList<>(new ArrayList<>(), 0, 0, 0);
    }

    /**
     * Creates empty DataList with maximum elements number
     *
     * @param totalCount maximum elements in DataList
     * @param <T>        item type
     * @return DataList instance without elements
     */
    public static <T> DataList<T> emptyWithTotal(int totalCount) {
        return new DataList<>(new ArrayList<>(), 0, 0, totalCount);
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
     * Merges two DataLists.
     *
     * @param data next portion of data
     * @return current DataList instance with merged data
     * @throws IncompatibleRangesException when the other DataList's offset is bigger than data count.
     */
    public DataList<T> merge(DataList<T> data) {
        ArrayList<T> merged = performDataMerge(data);
        this.data.clear();
        this.data.addAll(merged);
        recalculateDataRanges(data);
        return this;
    }

    /**
     * Merges two DataLists with remove of duplicated elements.
     * After removal there will remain only actual elements (last added).
     *
     * @param data              next portion of data
     * @param distinctPredicate predicate with duplicate removal logic
     * @return current DataList instance with merged data
     * @throws IncompatibleRangesException when the other DataList's offset is bigger than data count.
     */
    public <R> DataList<T> merge(DataList<T> data, MapFunc<R, T> distinctPredicate) {
        ArrayList<T> merged = performDataMerge(data);
        ArrayList<T> filtered = distinctByLast(merged, distinctPredicate);
        this.data.clear();
        this.data.addAll(filtered);
        recalculateDataRanges(data);
        return this;
    }

    /**
     * Transforms DataList from one type to another.
     *
     * @param mapFunc mapper function
     * @param <R>     new data type
     * @return DataList with elements of new type
     */
    public <R> DataList<R> transform(MapFunc<R, T> mapFunc) {
        List<R> resultData = new ArrayList<>();
        for (T item : this) {
            resultData.add(mapFunc.call(item));
        }

        return new DataList<>(resultData, limit, offset, totalCount);
    }

    /**
     * Gets the new offset value required to load next portion of data
     *
     * @return int
     */
    public int getNextOffset() {
        return limit + offset;
    }

    /**
     * Get count of elements in pagination portion
     *
     * @return int
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Get current pagination offset from the first element of list
     *
     * @return int
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Get maximum element count that list can hold.
     *
     * @return int
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * Checks if the list can load next portion of data, or is it full.
     *
     * @return boolean
     */
    public boolean canGetMore() {
        return totalCount > limit + offset;
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
    @NotNull
    public String toString() {
        return "DataList" +
                "{limit=" + limit +
                ", offset=" + offset +
                ", data=" + data +
                '}';
    }

    private ArrayList<T> performDataMerge(DataList<T> other) throws IncompatibleRangesException {
        boolean isReverse = other.offset < this.offset;
        DataList<T> source = isReverse ? this : other;
        DataList<T> destination = isReverse ? other : this;
        return tryMerge(destination, source);
    }

    private ArrayList<T> tryMerge(DataList<T> to, DataList<T> from) {
        if (canBeMerged(to, from)) {
            return merge(
                    to.data,
                    from.data,
                    from.offset - to.offset
            );
        } else {
            throw new IncompatibleRangesException();
        }
    }

    private ArrayList<T> merge(ArrayList<T> to, ArrayList<T> from, int start) {
        ArrayList<T> result = new ArrayList<>();
        boolean hasStartCollision = start < to.size();
        List<T> destinationElements = hasStartCollision ? to.subList(0, start) : to;
        result.addAll(destinationElements);
        result.addAll(from);
        return result;
    }

    private boolean canBeMerged(DataList<T> to, DataList<T> from) {
        int destinationItemsCount = to.offset + to.limit;
        return destinationItemsCount >= from.offset;
    }

    private void recalculateDataRanges(DataList<T> other) {
        boolean isReverse = other.offset < this.offset;

        if (isReverse) { //reverse loading
            this.offset = other.offset;
            this.limit = size();
        } else if (this.offset == other.offset) { //collision
            this.limit = other.limit;
        } else { //normal loading
            this.limit = other.offset + other.limit - this.offset;
        }

        this.totalCount = other.totalCount;
    }

    /**
     * Delete duplicates of equal elements from the list.
     * Equality criteria is defined in distinctPredicate.
     *
     * @param source            source list
     * @param distinctPredicate equality criteria
     * @return filtered list without duplicates
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
