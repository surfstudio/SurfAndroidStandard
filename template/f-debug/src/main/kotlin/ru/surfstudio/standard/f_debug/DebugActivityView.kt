package ru.surfstudio.standard.f_debug

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider
import ru.surfstudio.standard.f_debug.controller.DebugItemController
import javax.inject.Inject

/**
 * Вью экрана показа информации для дебага
 */
class DebugActivityView : BaseRenderableActivityView<DebugScreenModel>() {

    @Inject
    lateinit var presenter: DebugPresenter

    private val adapter = EasyAdapter()

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_debug

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initRecycler()
        initAdapter()
    }

    override fun renderInternal(screenModel: DebugScreenModel) { }

    override fun getScreenName(): String = "debug"

    private fun initRecycler() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun initAdapter() {
        val showControllersItemController = createDebugItemController(R.string.show_controllers_debug_item) {
            presenter.openControllersScreen()
        }

        val showFcmTokenItemController = createDebugItemController(R.string.show_fcm_token_debug_item) {
            presenter.openFcmTokenScreen()
        }

        adapter.setItems(ItemList.create()
                .add(showControllersItemController)
                .add(showFcmTokenItemController))
    }

    private fun createDebugItemController(@StringRes nameId: Int,
                                          onClickListener: () -> Unit): DebugItemController {
        return DebugItemController(getString(nameId), onClickListener)
    }
}
