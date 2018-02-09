package ru.surfstudio.easyadapter.carousel

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.util.AttributeSet
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.impl.controller.BindableItemController
import ru.surfstudio.android.easyadapter.impl.holder.BindableViewHolder

/**
 * Вью-карусель элементов
 */
class CarouselView<T> @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : RecyclerView(context, attributeSet) {

    var centerItemChangedListener: (position: Int) -> Unit = {}

    private var realItemsCount = 0
    private val easyAdapter: EasyAdapter = EasyAdapter()
    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    var centerItemPosition: Int = 0
        set(value) {
            if (value != field) { //нас интересуют только отличающиеся от старого значения
                centerItemChangedListener.invoke(value)
            }
            field = value
        }

    var isInfinite = false
        set(value) {
            field = value
            easyAdapter.setInfiniteScroll(value)
            if (value) {
                applyInfiniteScroll()
            }
        }

    init {
        initAttrs(context, attributeSet)
        easyAdapter.setFirstInvisibleItemEnabled(false)

        this.layoutManager = linearLayoutManager
        this.adapter = easyAdapter

        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateCenterPosition()
            }
        })
    }

    fun render(elements: ItemList) {
        easyAdapter.setItems(elements)
        realItemsCount = elements.size
        if (isInfinite) applyInfiniteScroll()
    }

    fun render(elements: List<T>, itemController: BindableItemController<T, out BindableViewHolder<T>>) {
        easyAdapter.setItems(ItemList.create()
                .addAll(elements, itemController))
        realItemsCount = elements.size
        if (isInfinite) applyInfiniteScroll()
    }

    fun setSnapHelper(snapHelper: SnapHelper) {
        snapHelper.attachToRecyclerView(this)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CarouselView, 0, 0)
        try {
            isInfinite = ta.getBoolean(R.styleable.CarouselView_isInfinite, false) //должна ли быть карусель зацикленной
        } finally {
            ta.recycle()
        }
    }

    private fun updateCenterPosition() {
        if (realItemsCount != 0) {
            val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
            val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
            this.centerItemPosition = (lastVisiblePosition + firstVisiblePosition) / 2 % realItemsCount
        }
    }

    private fun applyInfiniteScroll() {
        val startPosition = EasyAdapter.INFINITE_SCROLL_LOOPS_COUNT / 2 * realItemsCount
        this.linearLayoutManager.scrollToPosition(startPosition)
    }
}