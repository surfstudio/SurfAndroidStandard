package ru.surfstudio.standard.f_feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.standard.f_feed.databinding.FragmentFeedBinding
import ru.surfstudio.standard.f_feed.di.FeedScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviFragmentView
import ru.surfstudio.standard.v_message_controller_top.IconMessageController
import javax.inject.Inject

internal class FeedFragmentView : BaseMviFragmentView<FeedState, FeedEvent>(), CrossFeatureFragment {

    @Inject
    override lateinit var hub: ScreenEventHub<FeedEvent>

    @Inject
    override lateinit var sh: FeedScreenStateHolder

    @Inject
    lateinit var messageController: IconMessageController

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
        if (state.message.isNotEmpty()) {
            messageController.show(message = state.message)
        }
    }

    override fun initViews() {
        binding.feedBtn.clicks().emit(FeedEvent.OpenDialog)
    }
}