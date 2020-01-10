package ru.surfstudio.android.easyadapter.sample.ui.screen.async

import android.os.Bundle
import android.os.PersistableBundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.async_inflate_list_layout.*
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.sample.R
import javax.inject.Inject

class AsyncInflateListActivityView : BaseRenderableActivityView<AsyncInflateListScreenModel>() {
    @Inject
    internal lateinit var presenter: AsyncInflateListPresenter

    private val adapter = EasyAdapter()

    private val controller = AsyncInflateItemController {
        toast("Click!")
    }

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> {
        return AsyncInflateListScreenConfigurator(intent)
    }

    override fun getScreenName() = "AsyncInflateListActivityView"

    override fun getContentView() = R.layout.async_inflate_list_layout

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun renderInternal(screenModel: AsyncInflateListScreenModel) {
        adapter.setItems(ItemList.create().addAll(screenModel.data, controller))
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        initRecycler()
    }

    private fun initRecycler() {
        async_inflate_rv.layoutManager = LinearLayoutManager(this)
        async_inflate_rv.adapter = adapter
    }
}