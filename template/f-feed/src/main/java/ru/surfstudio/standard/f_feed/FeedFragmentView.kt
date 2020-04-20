package ru.surfstudio.standard.f_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.standard.f_feed.di.FeedScreenConfigurator
import javax.inject.Inject

class FeedFragmentView: BaseRenderableFragmentView<FeedScreenModel>(), CrossFeatureFragment {

    @Inject
    lateinit var presenter: FeedPresenter

    override fun createConfigurator() = FeedScreenConfigurator()

    override fun getPresenters() = arrayOf(presenter)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_feed, container, false)

    override fun renderInternal(sm: FeedScreenModel?) {
        // TODO
    }
}