package ru.surfstudio.standard.f_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.standard.f_search.di.SearchScreenConfigurator
import javax.inject.Inject

class SearchFragmentView : BaseRxFragmentView(), CrossFeatureFragment {

    @Inject
    lateinit var bm: SearchBindModel

    override fun createConfigurator() = SearchScreenConfigurator()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

}