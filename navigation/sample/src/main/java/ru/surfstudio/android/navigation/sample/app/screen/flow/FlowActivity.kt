package ru.surfstudio.android.navigation.sample.app.screen.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_flow.*
import ru.surfstudio.android.navigation.command.activity.Finish
import ru.surfstudio.android.navigation.command.fragment.RemoveLast
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App

/**
 * Активити - контейнер для фрагментов с возможностью отображать
 * [FlowRoute.screenId] в текствью над дочерними фрагментами.
 */
class FlowActivity: AppCompatActivity(), FragmentNavigationContainer {

    private lateinit var route: FlowRoute

    override val containerId: Int = R.id.flow_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)

        route = FlowRoute(intent)

        flow_title_tv.text = route.screenId
    }

    override fun onBackPressed() {
        val screenId = route.getId()
        val backStackEntryCount = App.provider.provide()
                .fragmentNavigationProvider
                .provide(screenId)
                .fragmentNavigator.backStackEntryCount
        val command = if (backStackEntryCount > 1){
            RemoveLast(sourceTag = screenId)
        } else {
            Finish()
        }
        App.executor.execute(command)
    }
}