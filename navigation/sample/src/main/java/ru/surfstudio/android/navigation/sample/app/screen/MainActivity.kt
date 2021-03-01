package ru.surfstudio.android.navigation.sample.app.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.command.fragment.Add
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.splash.SplashRoute
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations

class MainActivity : AppCompatActivity(), FragmentNavigationContainer {

    override val containerId: Int = R.id.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        if (savedInstanceState == null) { //Adding fragment only on first create
            App.executor.execute(Add(SplashRoute(), FadeAnimations()))
//            val firstFlow = FlowRoute("first")
//            val secondFlow = FlowRoute("second")
//            App.executor.execute(
//                    listOf(
//                            Start(firstFlow),
//                            Replace(ImageRoute(0), sourceTag = firstFlow.getId()),
//                            Replace(ImageRoute(1), sourceTag = firstFlow.getId()),
//                            Replace(ImageRoute(2), sourceTag = firstFlow.getId()),
//                            Start(NumberRoute(0)),
//                            Start(secondFlow),
//                            Replace(ImageRoute(0), sourceTag = secondFlow.getId()),
//                            Replace(ImageRoute(1), sourceTag = secondFlow.getId()),
//                            Replace(ImageRoute(2), sourceTag = secondFlow.getId()),
//                            Start(NumberRoute(1))
//                    )
//            )
        }
    }
}