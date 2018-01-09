package ru.surfstudio.android.core.ui.base.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import ru.surfstudio.android.core.ui.base.recycler.controller.NoDataItemController;
import ru.surfstudio.android.core.ui.base.recycler.holder.BindableViewHolder;
import ru.surfstudio.android.core.ui.base.recycler.item.NoDataItem;
import ru.surfstudio.android.core.ui.base.screen.model.LdsSwrPgnScreenModel;
import ru.surfstudio.android.core.ui.base.screen.model.state.PaginationState;

/**
 * Адаптер с поддержкой пагинации, работает в связке с {@link LdsSwrPgnScreenModel}
 * Добавляет к списку футер с состояниями {@link PaginationState}
 * <p>
 * После эмита события о том что необходимо загрузить новый блок данных, события больше не
 * будут выбрасываться пока адаптеру не будет установлен {@link PaginationState#READY}
 * Также событие о необходимости подгрузить данные эмитится при клике на футер с состоянием
 * {@link PaginationState#ERROR}
 */
public abstract class BasePaginationableAdapter<H extends RecyclerView.ViewHolder> extends EasyAdapter {

    private OnShowMoreListener onShowMoreListener;

    public interface OnShowMoreListener {
        void onShowMore();
    }

    public BasePaginationableAdapter() {
        getPaginationFooterController().setListener(this::onShowMoreClick);
    }

    protected abstract BasePaginationFooterController getPaginationFooterController();

    public void setState(PaginationState state) {
        getPaginationFooterController().setState(state);
        autoNotify();
    }

    @Override
    public void setItems(ItemList items) {
        if (!items.isEmpty()) {
            items.addFooter(getPaginationFooterController());
        }
        super.setItems(items);
    }

    public void setOnShowMoreListener(OnShowMoreListener onShowMoreListener) {
        this.onShowMoreListener = onShowMoreListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        initLayoutManager(layoutManager);
        initPaginationListener(recyclerView, layoutManager);
    }

    private void initPaginationListener(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onShowMoreListener != null && !getPaginationFooterController().isBlockShowMoreEvent()) {
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    // инвертируем первый и последний видимый
                    // элемент если список снизу вверх
                    if (layoutManager.getReverseLayout()) {
                        int temp = firstVisibleItem;
                        firstVisibleItem = lastVisibleItem;
                        lastVisibleItem = temp;
                    }

                    int numVisibleItem = Math.abs(lastVisibleItem - firstVisibleItem);

                    if (totalItemCount - lastVisibleItem < 2 * numVisibleItem) {
                        getPaginationFooterController().setState(PaginationState.LOADING);
                        recyclerView.post(() -> {
                            onShowMoreListener.onShowMore();
                            autoNotify();
                        });
                    }
                }
            }
        });
    }

    private void setLoadingStatus() {
        setState(PaginationState.LOADING);
    }

    private void initLayoutManager(LinearLayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager castedLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup existingLookup = castedLayoutManager.getSpanSizeLookup();

            castedLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == getPaginationFooterPosition() &&
                            !items.isEmpty() &&
                            items.get(position).getItemController() instanceof BasePaginationFooterController) {
                        //footer должен быть на всю ширину экрана
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

    private void onShowMoreClick() {
        setLoadingStatus();
        if (onShowMoreListener != null) {
            onShowMoreListener.onShowMore();
        }
    }

    protected abstract static class BasePaginationFooterController<H extends BasePaginationFooterHolder> extends NoDataItemController<H> {
        private PaginationState state = PaginationState.COMPLETE;
        private OnShowMoreListener listener;

        public void setListener(OnShowMoreListener listener) {
            this.listener = listener;
        }

        public void setState(PaginationState state) {
            this.state = state;
        }

        public boolean isBlockShowMoreEvent() {
            return state != PaginationState.READY;
        }

        @Override
        public void bind(H holder, NoDataItem<H> item) {
            holder.bind(state);
        }

        @Override
        public H createViewHolder(ViewGroup parent) {
            return createViewHolder(parent, listener);
        }

        @Override
        public long getItemHash(NoDataItem<H> item) {
            return state.hashCode();
        }

        protected abstract H createViewHolder(ViewGroup parent, OnShowMoreListener listener);
    }

    protected static abstract class BasePaginationFooterHolder extends BindableViewHolder<PaginationState> {

        public BasePaginationFooterHolder(ViewGroup parent, @LayoutRes int layoutRes) {
            super(parent, layoutRes);
        }

    }

}
