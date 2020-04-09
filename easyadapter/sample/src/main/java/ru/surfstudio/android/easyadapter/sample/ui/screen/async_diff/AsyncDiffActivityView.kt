package ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff

import android.os.Bundle
import android.os.PersistableBundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.layout_async_diff_list.*
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.diff.async.AsyncDiffStrategy
import ru.surfstudio.android.easyadapter.sample.R
import javax.inject.Inject

class AsyncDiffActivityView : BaseRenderableActivityView<AsyncDiffScreenModel>() {

    @Inject
    internal lateinit var presenter: AsyncDiffPresenter

    private val adapter = EasyAdapter()

    private val controller = AsyncDiffItemController { toast("Click!") }

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> =
            AsyncDiffScreenConfigurator(intent)


    override fun getScreenName() = "AsyncDiffActivityView"

    override fun getContentView() = R.layout.layout_async_diff_list

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListener()
        initRecycler()
    }

    override fun renderInternal(screenModel: AsyncDiffScreenModel) {
        adapter.setItems(ItemList.create().addAll(screenModel.data, controller))
    }

    private fun initListener() {
        async_diff_generate_btn.setOnClickListener { presenter.generateNewDataList() }
    }

    private fun initRecycler() {
        async_diff_rv.layoutManager = LinearLayoutManager(this)
        async_diff_rv.adapter = adapter.apply {
            isFirstInvisibleItemEnabled = true
            setAsyncDiffCalculationEnabled(true)
            setDiffResultDispatchListener { toast("Diff dispatched!") }
            setAsyncDiffStrategy(AsyncDiffStrategy.APPLY_LATEST)
        }
    }
}