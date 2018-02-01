package ru.surfstudio.standard.ui.common.widget.carousel

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import ru.surfstudio.android.core.app.log.Logger
import ru.surfstudio.android.core.ui.base.recycler.EasyAdapter
import ru.surfstudio.android.core.ui.base.recycler.ItemList
import ru.surfstudio.android.core.ui.base.recycler.controller.BindableItemController
import ru.surfstudio.android.core.ui.base.recycler.holder.BindableViewHolder
import ru.surfstudio.standard.R

/**
 * Вью-карусель элементов
 */
//TODO: сделать наследником RecyclerView
class CarouselView<T> @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : RecyclerView(context, attributeSet) {

    var centerItem: T? = null
        set(value) {
            value?.let {
                if (it != field) { //нас интересуют только отличающиеся от старого значения
                    centerItemChangedListener.invoke(value)
                }
            }
            field = value
        }

    var elements: List<T> = emptyList()

    var centerItemChangedListener: (T) -> Unit = {}

    private val easyAdapter: EasyAdapter = EasyAdapter()

    private var isLooped = false
        set(value) {
            easyAdapter.setInfiniteScroll(value)
            if (value) {
                this.linearLayoutManager.scrollToPosition(EasyAdapter.INFINITE_SCROLL_FAKE_COUNT / 2 - ((EasyAdapter.INFINITE_SCROLL_FAKE_COUNT / 2) % 9))
            }
            field = value
        }

    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    init {
        initAttrs(context, attributeSet)
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
    }

    fun render(elements: List<T>, itemController: BindableItemController<T, out BindableViewHolder<T>>) {
        this.elements = elements
        easyAdapter.setItems(ItemList.create()
                .addAll(elements, itemController))
    }

    /**
     * Устанавливает бесконечную прокрутку списка
     */
    fun setInfinite(isLooped: Boolean) {
        this.isLooped = isLooped
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CarouselView, 0, 0)
        try {
            isLooped = ta.getBoolean(R.styleable.CarouselView_isLooped, false) //должна ли быть карусель зацикленной
        } finally {
            ta.recycle()
        }
    }

    private fun updateCenterPosition() {
        val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
        val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val centerItemPosition = if (elements.isNotEmpty())
            (lastVisibleItemPosition + firstVisibleItemPosition) / 2 % elements.size
        else 0
        this.centerItem = elements[centerItemPosition]
        Logger.d("Center Position = " + centerItemPosition)
    }

}