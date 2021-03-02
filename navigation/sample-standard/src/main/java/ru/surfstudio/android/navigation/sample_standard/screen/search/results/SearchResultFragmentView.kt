package ru.surfstudio.android.navigation.sample_standard.screen.search.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_search_result.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.navigation.sample_standard.R
import ru.surfstudio.android.navigation.sample_standard.utils.addOnBackPressedListener
import javax.inject.Inject

class SearchResultFragmentView: BaseRxFragmentView() {

    @Inject
    lateinit var bm: SearchResultBindModel

    override fun createConfigurator() = SearchResultScreenConfigurator(requireArguments())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        addOnBackPressedListener { bm.backClick.accept() }
        search_result_back_btn.setOnClickListener { bm.backClick.accept() }

        bm.textState.bindTo { text: String ->
            search_result_tv.text = text
        }
    }
}