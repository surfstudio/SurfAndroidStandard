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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import ru.surfstudio.android.datalistbase.BaseDataList;
import ru.surfstudio.android.datalistbase.IncompatibleRangesException;
import ru.surfstudio.android.logger.Logger;

/**
 * List для работы с пагинацией
 * Механизм page-count
 * Можно сливать с другим DataList
 *
 * @param <T> Item
 */
public class DataList<T> implements BaseDataList<T> {

    public static final int UNSPECIFIED_PAGE = -1;
    public static final int UNSPECIFIED_PAGE_SIZE = -1;
    public static final int UNSPECIFIED_TOTAL_ITEMS_COUNT = -1;
    public static final int UNSPECIFIED_TOTAL_PAGES_COUNT = -1;

    //размер страницы
    private int pageSize;
    //с какой страницы начинается
    private int startPage;
    //количество страниц
    private int numPages;
    //максимальное количество элементов(опционально)
    private int totalItemsCount;
    //максимальное количество страниц(опционально)
    private int totalPagesCount;

    private ArrayList<T> data;

    /**
     * Создает dataList, начиная с некоторой страницы
     *
     * @param data     коллекция данных
     * @param page     номер страницы
     * @param pageSize размер страницы(кол-во элементов)
     */
    public DataList(Collection<T> data, int page, int pageSize) {
        this(data, page, 1, pageSize);
    }

    /**
     * Создает DataList , начиная с некоторой страницы ,
     * с возможностью задавать максимальное количество элементов и страниц
     *
     * @param data            коллекция данных
     * @param page            номер страницы
     * @param pageSize        размер страницы
     * @param totalItemsCount максимальное количество элементов
     * @param totalPagesCount максимальное количество страниц
     */
    public DataList(Collection<T> data, int page, int pageSize, int totalItemsCount, int totalPagesCount) {
        this(data, page, 1, pageSize, totalItemsCount, totalPagesCount);
    }

    /**
     * Создает dataList, начиная с некоторой страницы
     *
     * @param data     коллекция данных
     * @param page     номер страницы
     * @param numPages количество страниц
     * @param pageSize размер страницы(кол-во элементов)
     */
    public DataList(Collection<T> data, int page, int numPages, int pageSize) {
        this(data, page, numPages, pageSize, UNSPECIFIED_TOTAL_ITEMS_COUNT, UNSPECIFIED_TOTAL_PAGES_COUNT);
    }

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
     * Создает пустой DataList
     *
     * @param <T> тип данных в листе
     * @return пустой дата-лист
     */
    public static <T> DataList<T> empty() {
        return emptyWithTotalCount(0, 0);
    }

    /**
     * Создает пустой DataList
     *
     * @param <T> тип данных в листе
     * @return пустой дата-лист
     */
    public static <T> DataList<T> emptyUnspecifiedTotal() {
        return emptyWithTotalCount(UNSPECIFIED_TOTAL_ITEMS_COUNT, UNSPECIFIED_TOTAL_PAGES_COUNT);
    }

    /**
     * Создает пустой DataList
     *
     * @param <T>             тип данных в листе
     * @param totalItemsCount максимальное количество элементов
     * @param totalPagesCount максимальное количество страниц
     * @return пустой дата-лист
     */
    public static <T> DataList<T> emptyWithTotalCount(Integer totalItemsCount, Integer totalPagesCount) {
        return new DataList<>(new ArrayList<>(), UNSPECIFIED_PAGE, 0, UNSPECIFIED_PAGE_SIZE, totalItemsCount, totalPagesCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataList<T> merge(BaseDataList<T> data) {
        if (!(data instanceof DataList)) {
            throw new IllegalArgumentException("Cannot merge datalist type of " + data.getClass() + " to " + this.getClass());
        }
        DataList<T> inputDataList = (DataList<T>) data;
        if (this.startPage != UNSPECIFIED_PAGE
                && inputDataList.startPage != UNSPECIFIED_PAGE
                && this.pageSize != inputDataList.pageSize) {
            throw new IllegalArgumentException("pageSize for merging DataList must be same");
        }
        Map<Integer, List<T>> originalPagesData = split();
        Map<Integer, List<T>> inputPagesData = inputDataList.split();
        SortedMap<Integer, List<T>> resultPagesData = new TreeMap<>();
        resultPagesData.putAll(originalPagesData);
        resultPagesData.putAll(inputPagesData);

        ArrayList<T> newData = new ArrayList<>();
        int lastPage = UNSPECIFIED_PAGE;
        int lastPageItemsSize = UNSPECIFIED_PAGE_SIZE;
        for (Map.Entry<Integer, List<T>> pageData : resultPagesData.entrySet()) {
            Integer pageNumber = pageData.getKey();
            List<T> pageItems = pageData.getValue();
            if (lastPage != UNSPECIFIED_PAGE &&
                    (pageNumber - lastPage > 1 || lastPageItemsSize < pageSize)) {
                Logger.e(new IncompatibleRangesException("Merging DataLists has empty space " +
                        "between its ranges, original list: " + this + ", inputList: " + inputDataList));
                break;
            }
            lastPage = pageNumber;
            lastPageItemsSize = pageItems.size();
            newData.addAll(pageItems);
        }
        this.data = newData;
        this.startPage = resultPagesData.firstKey();
        this.numPages = lastPage - startPage + 1;
        this.totalItemsCount = inputDataList.totalItemsCount == UNSPECIFIED_TOTAL_ITEMS_COUNT
                ? this.totalItemsCount
                : inputDataList.totalItemsCount;
        this.totalPagesCount = inputDataList.totalPagesCount == UNSPECIFIED_TOTAL_PAGES_COUNT
                ? this.totalPagesCount
                : inputDataList.totalPagesCount;
        if (inputDataList.pageSize != UNSPECIFIED_PAGE_SIZE) {
            this.pageSize = inputDataList.pageSize;
        }
        return this;
    }

    /**
     * Не поддерживается
     */
    @Override
    public <R> BaseDataList<T> merge(BaseDataList<T> data, MapFunc<R, T> distinctPredicate) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> DataList<R> transform(MapFunc<R, T> mapFunc) {
        List<R> resultData = new ArrayList<>();
        for (T item : this) {
            resultData.add(mapFunc.call(item));
        }
        return new DataList<>(resultData, startPage, numPages, pageSize, this.totalItemsCount, this.totalPagesCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canGetMore() {
        return startPage == UNSPECIFIED_PAGE
                || (data.size() == (numPages - startPage + 1) * pageSize
                && totalPagesCount != numPages);
    }

    /**
     * @return размер одной страницы
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @return первая страница
     */
    public int getStartPage() {
        return startPage;
    }

    /**
     * @return количество страниц
     */
    public int getNumPages() {
        return numPages;
    }

    /**
     * возвращает значение page, c которого нужно начать чтобы подгрузить следующий блок данных
     */
    public int getNextPage() {
        return startPage == UNSPECIFIED_PAGE ? 1 : startPage + numPages;
    }

    /**
     * @return возможное количество элементов для загружки
     */
    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    /**
     * @return возможное количество страниц для загрузки
     */
    public int getTotalPagesCount() {
        return totalPagesCount;
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

    /**
     * разделяет данные на блоки по страницам
     *
     * @return
     */
    private Map<Integer, List<T>> split() {
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
}