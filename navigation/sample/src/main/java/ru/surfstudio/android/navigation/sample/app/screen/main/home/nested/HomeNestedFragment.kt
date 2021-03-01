package ru.surfstudio.android.navigation.sample.app.screen.main.home.nested

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home_nested.*
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App

class HomeNestedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_nested, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val currentOrder = HomeNestedRoute(arguments).order
        home_nested_tv.text = "Home\nNested\nFragment\n#$currentOrder"
        val nextOrder = currentOrder + 1
        home_nested_add_btn.setOnClickListener { App.executor.execute(Replace(HomeNestedRoute(nextOrder), sourceTag = tag!!)) }
    }

}