package ru.surfstudio.standard.f_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.standard.f_search.databinding.FragmentSearchBinding
import ru.surfstudio.standard.f_search.di.SearchScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviFragmentView
import javax.inject.Inject

/**
 * Вью таба поиск
 */
internal class SearchFragmentView : BaseMviFragmentView<SearchState, SearchEvent>(), CrossFeatureFragment {

    @Inject
    override lateinit var hub: ScreenEventHub<SearchEvent>

    @Inject
    override lateinit var sh: SearchScreenStateHolder
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun createConfigurator() = SearchScreenConfigurator(arguments)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun render(state: SearchState) {
    }

    override fun initViews() {
    }
}