package ru.surfstudio.android.navigation.sample.app.screen.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.command.fragment.ReplaceHard
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.auth.AuthRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.profile.about.AboutRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.profile.settings.ApplicationSettingsRoute
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations

class ProfileTabFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profile_settings_btn.setOnClickListener { App.navigator.execute(Start(ApplicationSettingsRoute())) }
        profile_about_app_btn.setOnClickListener { App.navigator.execute(Start(AboutRoute())) }
        profile_logout_btn.setOnClickListener { App.navigator.execute(ReplaceHard(AuthRoute(), FadeAnimations())) }
    }
}