package ru.surfstudio.standard.f_debug.reused_components

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_reused_components_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.f_debug.reused_components.controllers.CustomControllerDescriptionItemController
import ru.surfstudio.standard.f_debug.reused_components.description.addDescription
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.ReusedComponentsDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана для показа переиспользуемых компонентов
 */
class ReusedComponentsDebugActivityView : BaseRenderableActivityView<ReusedComponentsDebugScreenModel>() {

    @Inject
    lateinit var presenter: ReusedComponentsDebugPresenter

    private val adapter = EasyAdapter()

    private val sampleController = CustomControllerDescriptionItemController()

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ReusedComponentsDebugScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_reused_components_debug

    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initRecycler()
        initAdapter()
    }

    override fun renderInternal(sm: ReusedComponentsDebugScreenModel) {}

    override fun getScreenName(): String = "debug_controllers"

    private fun initRecycler() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun initAdapter() {
        adapter.setItems(ItemList.create()
                .addDescription("Пример использования")
                .add("Контроллеры", sampleController)
                .add("Данные", sampleController)
        )
    }
}
