package ru.surfstudio.android.navigation.sample.app.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.command.activity.Replace
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.number.NumberRoute
import ru.surfstudio.android.navigation.sample.app.utils.animations.SlideAnimations

class MainActivity : AppCompatActivity(), FragmentNavigationContainer {

    override val containerId: Int = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        if (savedInstanceState == null) { //Adding fragment only on first create
            App.executor.execute(listOf(Replace(NumberRoute(0),  animations = SlideAnimations()), Start(NumberRoute(1)), Start(NumberRoute(2)), Start(NumberRoute(3))))
        }
    }
}