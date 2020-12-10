package ru.surfstudio.standard.f_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.standard.f_feed.databinding.FragmentFeedBinding
import ru.surfstudio.standard.f_feed.di.FeedScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviFragmentView
import ru.surfstudio.standard.ui.util.view_binding.viewBinding
import javax.inject.Inject

internal class FeedFragmentView : BaseMviFragmentView<FeedState, FeedEvent>(), CrossFeatureFragment {

    @Inject
    override lateinit var hub: ScreenEventHub<FeedEvent>

    @Inject
    override lateinit var sh: FeedScreenStateHolder

    private val binding by viewBinding(FragmentFeedBinding::bind)

    override fun createConfigurator() = FeedScreenConfigurator(arguments)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun render(state: FeedState) {
    }

    override fun initViews() {
    }
}