package ru.surfstudio.standard.ui.common.contentcontainer

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.ui.base.screen.configurator.ViewConfigurator
import ru.surfstudio.android.core.ui.base.screen.fragment.BaseHandleableErrorFragmentView
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter
import ru.surfstudio.android.core.ui.base.screen.view.ContentContainerView
import ru.surfstudio.standard.R
import javax.inject.Inject


/**
 * Вью рутового экрана для дочерних фрагментов
 */
class ContentContainerFragmentView : BaseHandleableErrorFragmentView(), ContentContainerView {
    @get:Inject
    private var presenter: ContentContainerPresenter? = null

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter as CorePresenter<*>)
    }

    override fun createScreenConfigurator(activity: Activity, args: Bundle): ViewConfigurator<*> {
        return ContentContainerScreenConfigurator(activity, args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_content_container, container, false)
    }

    override fun getContentContainerViewId(): Int {
        return R.id.content_container_view_container
    }
}
