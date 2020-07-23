package ru.surfstudio.standard.f_feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.standard.f_feed.di.FeedScreenConfigurator
import javax.inject.Inject

class FeedFragmentView : BaseRxFragmentView(), CrossFeatureFragment {

    @Inject
    lateinit var bm: FeedBindModel

    override fun createConfigurator() = FeedScreenConfigurator()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_feed, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onActivityCreated(savedInstanceState, viewRecreated)
        setClickListeners()

        button_qq.clicks().subscribe()
    }

    fun Fragment.setClickListeners() {
        (view as ViewGroup).children.forEach { view ->
            view.setOnClickListener {  }
        }
    }
}