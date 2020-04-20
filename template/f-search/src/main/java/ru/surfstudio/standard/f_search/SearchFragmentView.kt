package ru.surfstudio.standard.f_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.standard.f_search.di.SearchScreenConfigurator
import javax.inject.Inject

class SearchFragmentView : BaseRenderableFragmentView<SearchScreenModel>(), CrossFeatureFragment {

    @Inject
    lateinit var presenter: SearchPresenter

    override fun createConfigurator() = SearchScreenConfigurator()

    override fun getPresenters() = arrayOf(presenter)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun renderInternal(sm: SearchScreenModel?) {
        // TODO
    }
}