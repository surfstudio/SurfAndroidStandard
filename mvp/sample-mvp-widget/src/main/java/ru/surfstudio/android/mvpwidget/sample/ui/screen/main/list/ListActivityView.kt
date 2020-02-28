package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.list

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.mvpwidget.sample.R
import javax.inject.Inject

/**
 * Вью экрана Cписка виджетов
 */
class ListActivityView : BaseRenderableActivityView<ListScreenModel>() {

    @Inject
    lateinit var presenter: ListPresenter

    private lateinit var recyclerView: RecyclerView

    private val adapter = EasyAdapter()

    private val listItemController = ListItemController {
    }

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ListScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_list

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        findViews()
        initListeners()
        initRecyclerView()
    }

    override fun renderInternal(screenModel: ListScreenModel) {
        adapter.setItems(ItemList.create()
                .addAll(screenModel.itemList, listItemController))
    }

    private fun findViews() {
        recyclerView = findViewById(R.id.recycler)
    }

    private fun initListeners() {
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun getScreenName(): String = "list"
}
