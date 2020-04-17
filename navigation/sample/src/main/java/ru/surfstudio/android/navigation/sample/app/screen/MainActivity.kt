package ru.surfstudio.android.navigation.sample.app.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.command.fragment.Add
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.navigation.provider.id.IdentifiableScreen
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.splash.SplashRoute
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations

class MainActivity : AppCompatActivity(), IdentifiableScreen, FragmentNavigationContainer {

    override val containerId: Int = R.id.fragment_container

    override val screenId: String = "AuthActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        if (savedInstanceState == null) { //Adding fragment only on first create
            App.navigator.execute(Add(SplashRoute(), FadeAnimations()))
        }
    }
}