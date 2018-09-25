package ru.surfstudio.standard.f_debug.debug_controllers

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider
import ru.surfstudio.standard.f_debug.debug.controllers.CustomControllerDescriptionItemController
import ru.surfstudio.standard.f_debug.debug_controllers.description.addDescription
import javax.inject.Inject

/**
 * Вью экрана для показа контроллеров, используемых в приложении
 */
class DebugControllersActivityView : BaseRenderableActivityView<DebugControllersScreenModel>() {

    @Inject
    lateinit var presenter: DebugControllersPresenter

    private val adapter = EasyAdapter()

    private val sampleController = CustomControllerDescriptionItemController()

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_debug_controllers

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initRecycler()
        initAdapter()
    }

    override fun renderInternal(screenModel: DebugControllersScreenModel) { }

    override fun getScreenName(): String = "debug_controllers"

    private fun initRecycler() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun initAdapter() {
        adapter.setItems(ItemList.create()
                .addDescription("Пример использования")
                .add("Данные", sampleController)
        )
    }
}
