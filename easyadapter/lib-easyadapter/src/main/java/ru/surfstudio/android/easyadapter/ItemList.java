/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.easyadapter;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import ru.surfstudio.android.easyadapter.controller.BindableItemController;
import ru.surfstudio.android.easyadapter.controller.DoubleBindableItemController;
import ru.surfstudio.android.easyadapter.controller.NoDataItemController;
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder;
import ru.surfstudio.android.easyadapter.item.BaseItem;
import ru.surfstudio.android.easyadapter.item.BindableItem;
import ru.surfstudio.android.easyadapter.item.DoubleBindableItem;
import ru.surfstudio.android.easyadapter.item.NoDataItem;


/**
 * List of items for RecyclerView, used with {@link EasyAdapter} and constructed in a Builder manner.
 * <br>
 * It contains logic of combining items with different types into one list that will be displayed in a single {@link RecyclerView}.
 * <br>
 * Such behavior is achieved by binding the data to a {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} and providing uniqueness checking interface to RecyclerView through ItemControllers.
 *
 * @see ru.surfstudio.android.easyadapter.controller.BaseItemController
 * @see BaseItem
 */
public class ItemList extends ArrayList<BaseItem> {

    public interface BindableItemControllerProvider<T> {
        BindableItemController<T, ? extends BindableViewHolder<T>> provide(T data);
    }

    public ItemList(int initialCapacity) {
        super(initialCapacity);
    }

    public ItemList(Collection<? extends BaseItem> items) {
        super(items);
    }

    public ItemList() {
        //empty
    }

    /**
     * Create builder.
     *
     * @return new ItemList
     */
    public static ItemList create() {
        return new ItemList();
    }

    /**
     * Create builder with initial items.
     *
     * @param items items with
     * @return new ItemList with initial amount of items
     */
    public static ItemList create(Collection<BaseItem> items) {
        return new ItemList(items);
    }

    /**
     * Create builder with initial data and itemController.
     *
     * @param data           data to be displayed in View
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return new ItemList with initial amount of items
     */
    public static <T> ItemList create(Collection<T> data,
                                      final BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return create(data, data1 -> itemController);
    }

    /**
     * Create builder with initial data and provider for itemControllers.
     *
     * @param data                   data to be displayed in View
     * @param itemControllerProvider provider that provides controllers mapped to data type
     * @param <T>                    data Type
     * @return new ItemList with initial amount of items
     */
    public static <T> ItemList create(Collection<T> data,
                                      BindableItemControllerProvider<T> itemControllerProvider) {
        ItemList items = new ItemList(data.size());
        for (T dataItem : data) {
            items.addItem(new BindableItem<>(dataItem, itemControllerProvider.provide(dataItem)));
        }
        return items;
    }

    /**
     * Create builder with controller for element without data.
     *
     * @param itemController itemController to display static data
     * @return new ItemList with one item without data
     */
    public static ItemList create(NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return create().add(itemController);
    }

    //region Single Insert

    /**
     * Insert data to specific index of ItemList.
     *
     * @param index index at which data will be inserted
     * @param item  item that will be inserted
     * @return current instance of ItemList with inserted item
     */
    public ItemList insert(int index, BaseItem item) {
        this.add(index, item);
        return this;
    }

    /**
     * Insert data to specific index of ItemList under certain condition.
     *
     * @param condition will the data be inserted or not
     * @param index     index at which data will be inserted
     * @param baseItem  item that will be inserted
     * @return current instance of ItemList with or without inserted item
     */
    public ItemList insertIf(boolean condition,
                             int index,
                             BaseItem baseItem) {
        return condition ? insert(index, baseItem) : this;
    }

    /**
     * Insert data to specific index of ItemList.
     *
     * @param index          index at which data will be inserted
     * @param data           data to be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with inserted item
     */
    public <T> ItemList insert(int index,
                               T data,
                               BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insert(index, new BindableItem<>(data, itemController));
    }

    /**
     * Insert data to specific index of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param index          index at which data will be inserted
     * @param data           data to be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with or without inserted item
     */
    public <T> ItemList insertIf(boolean condition,
                                 int index,
                                 T data,
                                 BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertIf(condition, index, new BindableItem<>(data, itemController));
    }

    /**
     * Insert data to specific index of ItemList.
     *
     * @param index          index at which data will be inserted
     * @param firstData      first data to be inserted
     * @param secondData     second data to be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T1>           first data Type
     * @param <T2>           second data Type
     * @return current instance of ItemList with inserted item
     */
    public <T1, T2> ItemList insert(int index,
                                    T1 firstData,
                                    T2 secondData,
                                    DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return insert(index, new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    /**
     * Insert data to specific index of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param index          index at which data will be inserted
     * @param firstData      first data to be inserted
     * @param secondData     second data to be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T1>           first data Type
     * @param <T2>           second data Type
     * @return current instance of ItemList with or without inserted item
     */
    public <T1, T2> ItemList insertIf(boolean condition,
                                      int index,
                                      T1 firstData,
                                      T2 secondData,
                                      DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return insertIf(condition, index, new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    /**
     * Insert itemController without data to specific index of ItemList.
     *
     * @param index          index at which data will be inserted
     * @param itemController itemController without data
     * @return current instance of ItemList with inserted item
     */
    public ItemList insert(int index,
                           NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return this.insert(index, new NoDataItem<>(itemController));
    }

    /**
     * Insert itemController without data to specific index of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param index          index at which data will be inserted
     * @param itemController itemController without data
     * @return current instance of ItemList with or without inserted item
     */
    public ItemList insertIf(boolean condition,
                             int index,
                             NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return insertIf(condition, index, new NoDataItem<>(itemController));
    }

    //endregion
    //region Single Add

    /**
     * Add data to the end of ItemList.
     *
     * @param item item that will be inserted
     * @return current instance of ItemList with added item
     */
    public ItemList addItem(BaseItem item) {
        return insert(this.size(), item);
    }

    /**
     * Add data to the end of ItemList under certain condition.
     *
     * @param condition will the data be inserted or not
     * @param item      item that will be inserted
     * @return current instance of ItemList with or without added item
     */
    public ItemList addIf(boolean condition,
                          BaseItem item) {
        return condition ? addItem(item) : this;
    }

    /**
     * Add data to the  end of ItemList.
     *
     * @param data           data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with added item
     */
    public <T> ItemList add(T data,
                            BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addItem(new BindableItem<>(data, itemController));
    }

    /**
     * Add data to the end of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param data           data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with or without added item
     */
    public <T> ItemList addIf(boolean condition,
                              T data,
                              BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addIf(condition, new BindableItem<>(data, itemController));
    }

    /**
     * Add data to the end of ItemList.
     *
     * @param firstData      first data that will be inserted
     * @param secondData     second data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T1>           first data Type
     * @param <T2>           second data Type
     * @return current instance of ItemList with added item
     */
    public <T1, T2> ItemList add(T1 firstData,
                                 T2 secondData,
                                 DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addItem(new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    /**
     * Add data to the end of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param firstData      first data that will be inserted
     * @param secondData     second data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T1>           first data Type
     * @param <T2>           second data Type
     * @return current instance of ItemList with or without added item
     */
    public <T1, T2> ItemList addIf(boolean condition,
                                   T1 firstData,
                                   T2 secondData,
                                   DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addIf(condition, new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    /**
     * Add itemController without data to the end of ItemList.
     *
     * @param itemController itemController without data
     * @return current instance of ItemList with added item
     */
    public ItemList add(NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addItem(new NoDataItem<>(itemController));
    }


    /**
     * Add itemController without data to the end of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param itemController itemController without data
     * @return current instance of ItemList with or without added item
     */
    public ItemList addIf(boolean condition,
                          NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addIf(condition, new NoDataItem<>(itemController));
    }

    //endregion
    //region Collection Insert

    /**
     * Insert collection of data to specific index of ItemList.
     *
     * @param index index at which data will be inserted
     * @param items items that will be inserted
     * @return current instance of ItemList with inserted items
     */
    public ItemList insertAll(int index,
                              Collection<BaseItem> items) {
        this.addAll(index, items);
        return this;
    }

    /**
     * Insert collection of data to specific index of ItemList under certain condition.
     *
     * @param condition will the data be inserted or not
     * @param index     index at which data will be inserted
     * @param items     items that will be inserted
     * @return current instance of ItemList with or without inserted items
     */
    public ItemList insertAllIf(boolean condition,
                                int index,
                                Collection<BaseItem> items) {
        return condition ? insertAll(index, items) : this;
    }

    /**
     * Insert collection of data to specific index of ItemList.
     *
     * @param index          index at which data will be inserted
     * @param data           data to be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with inserted items
     */
    public <T> ItemList insertAll(int index,
                                  Collection<T> data,
                                  final BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAll(index, data, new BindableItemControllerProvider<T>() {
            @Override
            public BindableItemController<T, ? extends BindableViewHolder<T>> provide(T data) {
                return itemController;
            }
        });
    }

    /**
     * Insert collection of data to specific index of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param index          index at which data will be inserted
     * @param data           data to be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with or without inserted items
     */
    public <T> ItemList insertAllIf(boolean condition,
                                    int index,
                                    Collection<T> data,
                                    final BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAllIf(condition, index, data, new BindableItemControllerProvider<T>() {
            @Override
            public BindableItemController<T, ? extends BindableViewHolder<T>> provide(T data) {
                return itemController;
            }
        });
    }

    /**
     * Insert collection of data to specific index of ItemList.
     *
     * @param index                  index at which data will be inserted
     * @param data                   data to be inserted
     * @param itemControllerProvider provider that provides controllers mapped to data type
     * @param <T>                    data Type
     * @return current instance of ItemList with inserted items
     */
    public <T> ItemList insertAll(int index,
                                  Collection<T> data,
                                  BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAll(index, create(data, itemControllerProvider));
    }

    /**
     * Insert collection of data to specific index of ItemList under certain condition.
     *
     * @param condition              will the data be inserted or not
     * @param index                  index at which data will be inserted
     * @param data                   data to be inserted
     * @param itemControllerProvider provider that provides controllers mapped to data type
     * @param <T>                    data Type
     * @return current instance of ItemList with or without inserted items
     */
    public <T> ItemList insertAllIf(boolean condition,
                                    int index,
                                    Collection<T> data,
                                    BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAllIf(condition, index, create(data, itemControllerProvider));
    }

    //endregion
    //region Collection Add


    /**
     * Add collection of data to the end of ItemList.
     *
     * @param items item that will be inserted
     * @return current instance of ItemList with added items
     */
    public ItemList addAllItems(Collection<BaseItem> items) {
        return insertAll(this.size(), items);
    }

    /**
     * Add collection of data to the end of ItemList under certain condition.
     *
     * @param condition will the data be inserted or not
     * @param items     item that will be inserted
     * @return current instance of ItemList with or without added items
     */
    public ItemList addAllIf(boolean condition, Collection<BaseItem> items) {
        return insertAllIf(condition, this.size(), items);
    }


    /**
     * Add collection of data to the end of ItemList.
     *
     * @param data           collection of data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with added items
     */
    public <T> ItemList addAll(Collection<T> data,
                               BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAll(this.size(), data, itemController);
    }

    /**
     * Add collection of data to the end of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param data           collection of data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with or without added items
     */
    public <T> ItemList addAllIf(boolean condition,
                                 Collection<T> data,
                                 BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return insertAllIf(condition, this.size(), data, itemController);
    }

    /**
     * Add collection of data to the end of ItemList.
     *
     * @param data                   collection of data that will be inserted
     * @param itemControllerProvider provider that provides controllers mapped to data type
     * @param <T>                    data Type
     * @return current instance of ItemList with added items
     */
    public <T> ItemList addAll(Collection<T> data,
                               BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAll(this.size(), data, itemControllerProvider);
    }

    /**
     * Add collection of data to the end of ItemList under certain condition.
     *
     * @param condition              will the data be inserted or not
     * @param data                   collection of data that will be inserted
     * @param itemControllerProvider provider that provides controllers mapped to data type
     * @param <T>                    data Type
     * @return current instance of ItemList with or without added items
     */
    public <T> ItemList addAllIf(boolean condition,
                                 Collection<T> data,
                                 BindableItemControllerProvider<T> itemControllerProvider) {
        return insertAllIf(condition, this.size(), data, itemControllerProvider);
    }

    //endregion
    //region Headers

    /**
     * Add header to the start of ItemList.
     *
     * @param item item that will be inserted
     * @return current instance of ItemList with added header
     */
    public ItemList addHeader(BaseItem item) {
        return insert(0, item);
    }

    /**
     * Add header to the start of ItemList under the certain condition.
     *
     * @param condition will the data be inserted or not
     * @param item      item that will be inserted
     * @return current instance of ItemList with or without added header
     */
    public ItemList addHeaderIf(boolean condition, BaseItem item) {
        return condition ? addHeader(item) : this;
    }

    /**
     * Add header to the start of ItemList.
     *
     * @param data           data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with added header
     */
    public <T> ItemList addHeader(T data,
                                  BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addHeader(new BindableItem<>(data, itemController));
    }

    /**
     * Add header to the start of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param data           data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with or without added header
     */
    public <T> ItemList addHeaderIf(boolean condition,
                                    T data,
                                    BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addHeaderIf(condition, new BindableItem<>(data, itemController));
    }

    /**
     * Add header to the start of ItemList.
     *
     * @param firstData      first data that will be inserted
     * @param secondData     second data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T1>           first data Type
     * @param <T2>           second data Type
     * @return current instance of ItemList with added header
     */
    public <T1, T2> ItemList addHeader(T1 firstData,
                                       T2 secondData,
                                       DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addHeader(new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    /**
     * Add header to the start of ItemList under the certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param firstData      first data that will be inserted
     * @param secondData     second data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T1>           first data Type
     * @param <T2>           second data Type
     * @return current instance of ItemList with or without added header
     */
    public <T1, T2> ItemList addHeaderIf(boolean condition,
                                         T1 firstData,
                                         T2 secondData,
                                         DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addHeaderIf(condition, new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    /**
     * Add itemController without data to the start of ItemList.
     *
     * @param itemController itemController without data
     * @return current instance of ItemList with added header
     */
    public ItemList addHeader(NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addHeader(new NoDataItem<>(itemController));
    }


    /**
     * Add itemController without data to the start of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param itemController itemController without data
     * @return current instance of ItemList with or without added header
     */
    public ItemList addHeaderIf(boolean condition,
                                NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addHeaderIf(condition, new NoDataItem<>(itemController));
    }

    //endregion
    //region Footers

    /**
     * Add footer to the end of ItemList.
     *
     * @param item item that will be inserted
     * @return current instance of ItemList with added footer
     */
    public ItemList addFooter(BaseItem item) {
        return insert(this.size(), item);
    }

    /**
     * Add header to the end of ItemList under the certain condition.
     *
     * @param condition will the data be inserted or not
     * @param item      item that will be inserted
     * @return current instance of ItemList with or without added footer
     */
    public ItemList addFooterIf(boolean condition, BaseItem item) {
        return condition ? addFooter(item) : this;
    }

    /**
     * Add footer to the end of ItemList.
     *
     * @param data           data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with added footer
     */
    public <T> ItemList addFooter(T data,
                                  BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return addFooter(new BindableItem<>(data, itemController));
    }

    /**
     * Add footer to the end of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param data           data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T>            data Type
     * @return current instance of ItemList with or without added footer
     */
    public <T> ItemList addFooterIf(boolean condition,
                                    T data,
                                    BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        return condition ? addFooter(data, itemController) : this;
    }

    /**
     * Add footer to the end of ItemList.
     *
     * @param firstData      first data that will be inserted
     * @param secondData     second data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T1>           first data Type
     * @param <T1>           second data Type
     * @return current instance of ItemList with added footer
     */
    public <T1, T2> ItemList addFooter(T1 firstData,
                                       T2 secondData,
                                       DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addFooter(new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    /**
     * Add footer to the end of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param firstData      first data that will be inserted
     * @param secondData     second data that will be inserted
     * @param itemController controller to handle data and process it to {@link RecyclerView}
     * @param <T1>           first data Type
     * @param <T1>           second data Type
     * @return current instance of ItemList with or without added footer
     */
    public <T1, T2> ItemList addFooterIf(boolean condition,
                                         T1 firstData,
                                         T2 secondData,
                                         DoubleBindableItemController<T1, T2, ? extends RecyclerView.ViewHolder> itemController) {
        return addFooterIf(condition, new DoubleBindableItem<>(firstData, secondData, itemController));
    }

    /**
     * Add itemController without data to the end of ItemList under certain condition.
     *
     * @param itemController itemController without data
     * @return current instance of ItemList with or without added footer
     */
    public ItemList addFooter(NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addFooter(new NoDataItem<>(itemController));
    }

    /**
     * Add itemController without data to the end of ItemList under certain condition.
     *
     * @param condition      will the data be inserted or not
     * @param itemController itemController without data
     * @return current instance of ItemList with or without added footer
     */
    public ItemList addFooterIf(boolean condition,
                                NoDataItemController<? extends RecyclerView.ViewHolder> itemController) {
        return addFooterIf(condition, new NoDataItem<>(itemController));
    }
    //endregion
}
