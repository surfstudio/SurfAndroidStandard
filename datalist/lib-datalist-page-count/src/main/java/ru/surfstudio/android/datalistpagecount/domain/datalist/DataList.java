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
package ru.surfstudio.android.datalistpagecount.domain.datalist;


import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import ru.surfstudio.android.logger.Logger;

/**
 * Pagination list with page-count mechanism.
 * Two DataLists can be combined by calling merge function.
 * This function is the only way to merge pagination data blocks.
 *
 * @param <T> item type
 */
public class DataList<T> implements List<T>, Serializable {

    public static final int UNSPECIFIED_PAGE = -1;
    public static final int UNSPECIFIED_PAGE_SIZE = -1;
    public static final int UNSPECIFIED_TOTAL_ITEMS_COUNT = -1;
    public static final int UNSPECIFIED_TOTAL_PAGES_COUNT = -1;

    private int pageSize;
    private int startPage;
    private int numPages;
    private int totalItemsCount;
    private int totalPagesCount;

    private ArrayList<T> data;

    @FunctionalInterface
    public interface MapFunc<R, T> {
        R call(T item);
    }

    /**
     * Creates DataList starting from specific page
     *
     * @param data     collection with data
     * @param page     start page number
     * @param pageSize page size (count of elements on each page)
     */
    public DataList(Collection<T> data, int page, int pageSize) {
        this(data, page, 1, pageSize);
    }

    /**
     * Creates DataList starting from specific page
     * with maximum number of elements and maximum number of pages
     *
     * @param data            collection with data
     * @param page            start page number
     * @param pageSize        page size (count of elements on each page)
     * @param totalItemsCount maximum number of elements
     * @param totalPagesCount maximum number of pages
     */
    public DataList(Collection<T> data, int page, int pageSize, int totalItemsCount, int totalPagesCount) {
        this(data, page, 1, pageSize, totalItemsCount, totalPagesCount);
    }

    /**
     * Creates DataList starting from specific page
     *
     * @param data     collection with data
     * @param page     start page number
     * @param numPages current number of pages
     * @param pageSize page size (count of elements on each page)
     */
    public DataList(Collection<T> data, int page, int numPages, int pageSize) {
        this(data, page, numPages, pageSize, UNSPECIFIED_TOTAL_ITEMS_COUNT, UNSPECIFIED_TOTAL_PAGES_COUNT);
    }

    /**
     * Creates DataList starting from specific page
     * with maximum number of elements and pages
     *
     * @param data            collection with data
     * @param page            start page number
     * @param pageSize        page size (count of elements on each page)
     * @param numPages        current number of pages
     * @param totalItemsCount maximum number of elements
     * @param totalPagesCount maximum number of pages
     */
    public DataList(Collection<T> data, int page, int numPages, int pageSize, int totalItemsCount, int totalPagesCount) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
        this.startPage = page;
        this.pageSize = pageSize;
        this.numPages = numPages;
        this.totalItemsCount = totalItemsCount;
        this.totalPagesCount = totalPagesCount;
    }

    /**
     * Creates empty DataList with zero items and pages count
     *
     * @param <T> item type
     * @return Empty DataList
     */
    public static <T> DataList<T> empty() {
        return emptyWithTotalCount(0, 0);
    }

    /**
     * Creates empty DataList with unspecified total items and pages number.
     *
     * @param <T> item type
     * @return Empty DataList
     */
    public static <T> DataList<T> emptyUnspecifiedTotal() {
        return emptyWithTotalCount(UNSPECIFIED_TOTAL_ITEMS_COUNT, UNSPECIFIED_TOTAL_PAGES_COUNT);
    }


    /**
     * Creates empty DataList
     *
     * @param totalItemsCount maximum number of elements
     * @param totalPagesCount maximum number of pages
     * @param <T>             item type
     * @return Empty DataList
     */
    public static <T> DataList<T> emptyWithTotalCount(Integer totalItemsCount, Integer totalPagesCount) {
        return new DataList<>(new ArrayList<>(), UNSPECIFIED_PAGE, 0, UNSPECIFIED_PAGE_SIZE, totalItemsCount, totalPagesCount);
    }

    /**
     * Merges two DataLists.
     *
     * @param inputDataList next portion of data
     * @return current DataList instance with merged data
     * @throws IncompatibleRangesException when the other DataList's offset is bigger than data count.
     */
    public DataList<T> merge(DataList<T> inputDataList) {
        if (!isSamePageSize(inputDataList)) {
            throw new IllegalArgumentException("pageSize for merging DataList must be same");
        }
        SortedMap<Integer, List<T>> resultPagesData = mergePages(inputDataList);
        this.data = extractNewData(resultPagesData, inputDataList);
        calculateTotalCount(inputDataList);
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
        List<R> resultData = new ArrayList<R>();
        for (T item : this) {
            resultData.add(mapFunc.call(item));
        }
        return new DataList<>(resultData, startPage, numPages, pageSize, this.totalItemsCount, this.totalPagesCount);
    }

    /**
     * Gets the amount of elements in one page
     *
     * @return int
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Gets the number of first page
     *
     * @return int
     */
    public int getStartPage() {
        return startPage;
    }

    /**
     * Gets the current number of pages in list
     *
     * @return int
     */
    public int getNumPages() {
        return numPages;
    }

    /**
     * Gets the page number for next pagination request
     *
     * @return int
     */
    public int getNextPage() {
        return startPage == UNSPECIFIED_PAGE ? 1 : startPage + numPages;
    }

    /**
     * Gets maximum number of elements that list can hold
     *
     * @return int
     */
    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    /**
     * Gets maximum number of pages that list can hold
     *
     * @return int
     */
    public int getTotalPagesCount() {
        return totalPagesCount;
    }

    /**
     * Checks if the list can load next portion of data, or is it full.
     *
     * @return boolean
     */
    public boolean canGetMore() {
        return startPage == UNSPECIFIED_PAGE
                || (data.size() == (numPages - startPage + 1) * pageSize
                && totalPagesCount != numPages);
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
    public <T1> T1[] toArray(T1[] a) {
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
    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.data.clear();
        startPage = UNSPECIFIED_PAGE;
        pageSize = UNSPECIFIED_PAGE_SIZE;
        numPages = 0;
        totalItemsCount = UNSPECIFIED_TOTAL_ITEMS_COUNT;
        totalPagesCount = UNSPECIFIED_TOTAL_PAGES_COUNT;
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
        return data.subList(fromIndex, toIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataList)) return false;

        DataList<?> dataList = (DataList<?>) o;

        if (pageSize != dataList.pageSize) return false;
        if (startPage != dataList.startPage) return false;
        if (numPages != dataList.numPages) return false;
        return data != null ? data.equals(dataList.data) : dataList.data == null;

    }

    @Override
    public int hashCode() {
        int result = pageSize;
        result = 31 * result + startPage;
        result = 31 * result + numPages;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    @NotNull
    public String toString() {
        return "DataList{" +
                "pageSize=" + pageSize +
                ", startPage=" + startPage +
                ", numPages=" + numPages +
                ", totalItemsCount=" + totalItemsCount +
                ", totalPagesCount=" + totalPagesCount +
                ", data=" + data +
                '}';
    }

    private boolean isSamePageSize(DataList<T> other) {
        return this.startPage == UNSPECIFIED_PAGE
                || other.startPage == UNSPECIFIED_PAGE
                || this.pageSize == other.pageSize;
    }

    private SortedMap<Integer, List<T>> mergePages(DataList<T> other) {
        Map<Integer, List<T>> originalPagesData = splitByPages();
        Map<Integer, List<T>> inputPagesData = other.splitByPages();
        SortedMap<Integer, List<T>> resultPagesData = new TreeMap<>();
        resultPagesData.putAll(originalPagesData);
        resultPagesData.putAll(inputPagesData);
        return resultPagesData;
    }

    /**
     * Splits list by pages
     *
     * @return Map with page numbers as keys and lists with data as values
     */
    private Map<Integer, List<T>> splitByPages() {
        Map<Integer, List<T>> result = new HashMap<>();
        for (int i = startPage; i < startPage + numPages; i++) {
            int startItemIndex = (i - startPage) * pageSize;
            int itemsRemained = data.size() - startItemIndex;
            int endItemIndex = startItemIndex +
                    (itemsRemained < pageSize ? itemsRemained : pageSize);
            if (itemsRemained <= 0) break;

            result.put(i, data.subList(startItemIndex, endItemIndex));
        }
        return result;
    }

    private ArrayList<T> extractNewData(
            SortedMap<Integer, List<T>> resultPagesData,
            DataList<T> other
    ) {
        ArrayList<T> newData = new ArrayList<>();
        int lastPage = UNSPECIFIED_PAGE;
        int lastPageItemsSize = UNSPECIFIED_PAGE_SIZE;
        for (Map.Entry<Integer, List<T>> pageData : resultPagesData.entrySet()) {
            Integer pageNumber = pageData.getKey();
            List<T> pageItems = pageData.getValue();
            if (lastPage != UNSPECIFIED_PAGE &&
                    (pageNumber - lastPage > 1 || lastPageItemsSize < pageSize)) {
                Logger.e(new IncompatibleRangesException("Merging DataLists has empty space " +
                        "between its ranges, original list: " + this + ", inputList: " + other));
                break;
            }
            lastPage = pageNumber;
            lastPageItemsSize = pageItems.size();
            newData.addAll(pageItems);
        }
        this.startPage = resultPagesData.firstKey();
        this.numPages = lastPage - startPage + 1;
        return newData;
    }

    private void calculateTotalCount(DataList<T> inputDataList) {
        this.totalItemsCount = inputDataList.totalItemsCount == UNSPECIFIED_TOTAL_ITEMS_COUNT
                ? this.totalItemsCount
                : inputDataList.totalItemsCount;
        this.totalPagesCount = inputDataList.totalPagesCount == UNSPECIFIED_TOTAL_PAGES_COUNT
                ? this.totalPagesCount
                : inputDataList.totalPagesCount;
        if (inputDataList.pageSize != UNSPECIFIED_PAGE_SIZE) {
            this.pageSize = inputDataList.pageSize;
        }
    }
}