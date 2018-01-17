package ru.surfstudio.standard.ui.common.widget.carousel

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.ui.base.recycler.EasyAdapter
import ru.surfstudio.android.core.ui.base.recycler.ItemList
import ru.surfstudio.android.core.ui.base.recycler.controller.BindableItemController
import ru.surfstudio.android.core.ui.base.recycler.holder.BindableViewHolder
import ru.surfstudio.standard.R

/**
 * Вью-карусель элементов
 */
//TODO: сделать наследником RecyclerView
class CarouselView<T> @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : ViewGroup(context, attributeSet) {

    lateinit var itemController: BindableItemController<T, BindableViewHolder<T>>

    private lateinit var recycler: RecyclerView
    private val adapter: EasyAdapter = EasyAdapter()

    private var isLooped = false

    init {
        View.inflate(context, R.layout.carousel_view, null)
        initAttrs(context, attributeSet)
        initRecycler()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun render(elements: List<T>) {
        adapter.setItems(ItemList.create()
                .addAll(elements, itemController)) //itemController может быть не проинициализирован в данный момент TODO: обработать это
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CarouselView, 0, 0)
        try {
            isLooped = ta.getBoolean(R.styleable.CarouselView_isLooped, false) //должна ли быть карусель зацикленной
        } finally {
            ta.recycle()
        }
    }

    private fun initRecycler() {
        recycler = findViewById(R.id.carousel_view_rv)//TODO:  переделать на anko, когда зальют ветку с ней
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter
    }

}