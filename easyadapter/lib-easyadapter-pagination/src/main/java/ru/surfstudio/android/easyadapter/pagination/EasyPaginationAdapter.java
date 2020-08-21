/*
 * Copyright 2016 Maxim Tuev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.easyadapter.pagination;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Collection;

import ru.surfstudio.android.easyadapter.EasyAdapter;
import ru.surfstudio.android.easyadapter.ItemList;
import ru.surfstudio.android.easyadapter.controller.BindableItemController;
import ru.surfstudio.android.easyadapter.controller.NoDataItemController;
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder;
import ru.surfstudio.android.easyadapter.item.BaseItem;
import ru.surfstudio.android.easyadapter.item.NoDataItem;


/**
 * Adapter with pagination Footer based on {@link EasyAdapter}
 * <br>
 * It will emit the {@link OnShowMoreListener#onShowMore()}-event when user scrolls to bottom or clicks on footer with state {@link PaginationState#ERROR}.
 * <br>
 * It can emit this event on user's scroll only if the state is {@link PaginationState#READY}
 * <br>
 * To use this adapter in your project you must implement
 * {@link BasePaginationFooterController} and {@link OnShowMoreListener} for constructor params
 */
public class EasyPaginationAdapter extends EasyAdapter {

    private OnShowMoreListener onShowMoreListener;
    private boolean blockShowMoreEvent = true;
    private BasePaginationFooterController<? extends RecyclerView.ViewHolder> paginationFooterController;

    public EasyPaginationAdapter(
            BasePaginationFooterController<? extends RecyclerView.ViewHolder> paginationFooterController,
            OnShowMoreListener onShowMoreListener
    ) {
        setOnShowMoreListener(onShowMoreListener);
        this.paginationFooterController = paginationFooterController;
        this.paginationFooterController.setListener(this::onShowMoreClick);
    }

    /**
     * Set the items to adapter.
     *
     * @param items data that will be displayed
     * @param state current pagination state of the data
     */
    public void setItems(@NonNull ItemList items, @NonNull PaginationState state) {
        blockShowMoreEvent = state != PaginationState.READY;
        paginationFooterController.setState(state);
        items.add(paginationFooterController);
        super.setItems(items);
    }

    /**
     * Use {@link #setItems(ItemList, PaginationState)} instead.
     */
    @Override
    @Deprecated
    public void setItems(@NonNull ItemList items) {
        throw new UnsupportedOperationException("Use setItems(ItemList, PaginationState) instead");
    }

    public <T> void setData(@NonNull Collection<T> data,
                            @NonNull BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController,
                            @NonNull PaginationState paginationState) {
        setItems(ItemList.create(data, itemController), paginationState);
    }

    /**
     * Use {@link #setData(Collection, BindableItemController, PaginationState)} instead.
     */
    @Override
    @Deprecated
    public <T> void setData(@NonNull Collection<T> data,
                            @NonNull BindableItemController<T, ? extends RecyclerView.ViewHolder> itemController) {
        throw new UnsupportedOperationException("Use setData(Collection, BindableItemController, PaginationState) instead");
    }

    /**
     * Set the listener which will be triggered the load of next page.
     *
     * @param onShowMoreListener listener
     */
    public void setOnShowMoreListener(OnShowMoreListener onShowMoreListener) {
        this.onShowMoreListener = onShowMoreListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        initLayoutManager(layoutManager);
        initPaginationListener(recyclerView);
    }

    /**
     * Function which checks if it's needed to show more elements.
     * Can be overridden in descendant class.
     *
     * @param recyclerView RecyclerView for check condition
     * @return true if more elements can be shown
     */
    protected boolean shouldShowMoreElements(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItem = findFirstVisibleItem(layoutManager);
            int lastVisibleItem = findLastVisibleItem(layoutManager);
            int numVisibleItem = lastVisibleItem - firstVisibleItem;

            return totalItemCount - lastVisibleItem < 2 * numVisibleItem;
        }
        return false;
    }

    protected void initPaginationListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onShowMoreListener != null &&
                        !blockShowMoreEvent &&
                        shouldShowMoreElements(recyclerView)
                ) {
                    blockShowMoreEvent = true;
                    onShowMoreListener.onShowMore();
                }
            }
        });
    }

    /**
     * Find the first visible item position.
     * <p>
     * You should override this method if you're using custom LayoutManager or you want to start loading sooner or later.
     *
     * @param layoutManager layoutManager of adapter's RecyclerView
     * @return position
     */
    protected int findFirstVisibleItem(RecyclerView.LayoutManager layoutManager) {
        int pos = 0;

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) layoutManager;

            int[] items = lm.findFirstVisibleItemPositions(new int[lm.getSpanCount()]);
            int position = 0;

            for (int i = 0; i < items.length - 1; i++) {
                position = Math.min(items[i], items[i + 1]);
            }
            pos = position;
        }

        if (layoutManager instanceof LinearLayoutManager) {
            pos = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        return pos;
    }

    /**
     * Find the last visible item position.
     * <p>
     * You should override this method if you're using custom LayoutManager or you want to start loading sooner or later.
     *
     * @param layoutManager layoutManager of adapter's RecyclerView
     * @return position
     */
    protected int findLastVisibleItem(RecyclerView.LayoutManager layoutManager) {
        int pos = 0;

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) layoutManager;

            int spanCount = lm.getSpanCount();
            int[] items = lm.findLastVisibleItemPositions(new int[spanCount]);
            int position = 0;

            for (int i = 0; i < items.length - 1; i++) {
                position = Math.max(items[i], items[i + 1]);
            }
            pos = position;
        }

        if (layoutManager instanceof LinearLayoutManager) {
            pos = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        return pos;
    }

    /**
     * Initialization of layoutManager.
     * <p>
     * You should override if you're using custom layout manager and you want to display the footer loader
     * as desired, for example for a full width in {@link GridLayoutManager}.
     *
     * @param layoutManager layoutManager of adapter's RecyclerView
     */
    protected void initLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager castedLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup existingLookup = castedLayoutManager.getSpanSizeLookup();

            castedLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == getPaginationFooterPosition() && hasFooter()) {
                        //full width footer
                        return castedLayoutManager.getSpanCount();
                    } else {
                        return existingLookup.getSpanSize(position);
                    }
                }
            });
        }
    }

    private int getPaginationFooterPosition() {
        return getItemCount() - 1;
    }

    private boolean hasFooter() {
        return paginationFooterController.getState().isVisible();
    }

    private void onShowMoreClick() {
        setState(PaginationState.READY);
        if (onShowMoreListener != null) {
            onShowMoreListener.onShowMore();
        }
    }

    /**
     * Set the PaginationState to adapter
     *
     * @param state pagination state to be changed
     */
    public void setState(final PaginationState state) {
        blockShowMoreEvent = state != PaginationState.READY;
        ItemList items = getItems();
        int lastIndex = items.size() - 1;
        if (lastIndex >= 0 && hasFooter()) {
            items.remove(lastIndex);
        }
        paginationFooterController.setState(state);
        if (state.isVisible()) {
            items.add(paginationFooterController);
        }
        setItems(items, true);
    }

    /**
     * Listener that is invoked when adapter is ready to load more pages
     */
    public interface OnShowMoreListener {
        void onShowMore();
    }

    /**
     * Controller for a ViewHolder which is responsible for
     * displaying loading indicator when the next page isn't ready yet,
     * or display error message and "Retry" button when there's an error happened while loading the page.
     *
     * @param <H> ViewHolder type
     */
    public abstract static class BasePaginationFooterController<H extends BasePaginationFooterHolder> extends NoDataItemController<H> {
        private PaginationState state = PaginationState.COMPLETE;
        private OnShowMoreListener listener;

        /**
         * Set the listener which will be triggered the load of next page.
         *
         * @param listener listener
         */
        public void setListener(OnShowMoreListener listener) {
            this.listener = listener;
        }

        /**
         * Set the new pagination state
         *
         * @param state new state
         */
        public void setState(PaginationState state) {
            this.state = state;
        }

        /**
         * Get the pagination state
         *
         * @return current pagination state
         */
        public PaginationState getState() {
            return state;
        }

        /**
         * @see ru.surfstudio.android.easyadapter.controller.BaseItemController#bind(RecyclerView.ViewHolder, BaseItem)
         */
        @Override
        public void bind(H holder, NoDataItem<H> item) {
            holder.bind(state);
        }

        /**
         * @see ru.surfstudio.android.easyadapter.controller.BaseItemController#createViewHolder(ViewGroup)
         */
        @Override
        public H createViewHolder(ViewGroup parent) {
            return createViewHolder(parent, listener);
        }

        /**
         * @see ru.surfstudio.android.easyadapter.controller.BaseItemController#getItemHash(BaseItem)
         */
        @Override
        public String getItemHash(NoDataItem<H> item) {
            return String.valueOf(state.hashCode());
        }

        /**
         * Create {@link BasePaginationFooterHolder}.
         *
         * @param parent   parent view for ViewHolder
         * @param listener which will be triggered the load of next page
         * @return {@link BasePaginationFooterHolder}
         */
        protected abstract H createViewHolder(ViewGroup parent, OnShowMoreListener listener);
    }

    /**
     * {@link BindableViewHolder} that displays current {@link PaginationState}
     */
    public static abstract class BasePaginationFooterHolder extends BindableViewHolder<PaginationState> {
        public BasePaginationFooterHolder(ViewGroup parent, @LayoutRes int layoutRes) {
            super(parent, layoutRes);
        }
    }
}

