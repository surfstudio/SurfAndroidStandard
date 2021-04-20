package ru.surfstudio.android.navigation.sample_standard.screen.search.request

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import kotlinx.android.synthetic.main.fragment_search_request.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.navigation.sample_standard.R
import ru.surfstudio.android.navigation.sample_standard.utils.addOnBackPressedListener
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import javax.inject.Inject

/**
 * Экран ввода поискового запроса. Иммитирует экран поиска с подсказками
 */
class SearchRequestFragmentView : BaseRxFragmentView() {

    @Inject
    lateinit var bm: SearchRequestBindModel

    override fun createConfigurator() = SearchRequestScreenConfigurator(requireArguments())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_request, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        addOnBackPressedListener { bm.closeClick.accept() }
        search_request_close_btn.setOnClickListener { bm.closeClick.accept() }
        search_request_results_btn.setOnClickListener { bm.resultClick.accept() }
        search_request_input_et.doAfterTextChanged {
            bm.textChanges.accept(it?.toString() ?: EMPTY_STRING)
        }

        bm.textState.bindTo { text: String ->
            if (!TextUtils.equals(text, search_request_input_et.text))
                search_request_input_et.setText(text, TextView.BufferType.EDITABLE)
        }
    }
}