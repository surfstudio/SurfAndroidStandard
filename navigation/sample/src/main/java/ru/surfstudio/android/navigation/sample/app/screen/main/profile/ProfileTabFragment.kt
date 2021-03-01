package ru.surfstudio.android.navigation.sample.app.screen.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.command.fragment.ReplaceHard
import ru.surfstudio.android.navigation.observer.command.activity.StartForResult
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.auth.AuthRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.profile.about.AboutRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.profile.settings.ApplicationSettingsRoute
import ru.surfstudio.android.navigation.sample.app.screen.system.CameraHelper
import ru.surfstudio.android.navigation.sample.app.screen.system.CameraRoute
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations

class ProfileTabFragment : Fragment() {

    private val screenId = "ProfileTabFragment"
    private val targetRoute = AboutRoute()
    private val cameraRoute = CameraRoute(uniqueId = screenId)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profile_settings_btn.setOnClickListener { App.executor.execute(Start(ApplicationSettingsRoute())) }
        profile_about_app_btn.setOnClickListener { App.executor.execute(Start(AboutRoute())) }
        profile_logout_btn.setOnClickListener { App.executor.execute(ReplaceHard(AuthRoute(), FadeAnimations())) }
        profile_attach_photo_btn.setOnClickListener {
            App.executor.execute(
                    StartForResult(
                            CameraRoute(
                                    uniqueId = screenId,
                                    chooserTitle = "Select photo for profile",
                                    takenPhotoFile = CameraHelper(requireContext()).generatePhotoPath()
                            )
                    )
            )
        }

        App.resultObserver.addListener(targetRoute, ::showAppName)
        App.resultObserver.addListener(cameraRoute, ::showResult)
    }

    private fun showAppName(name: String) {
        showToast("App name: $name")
    }

    private fun showResult(isSuccess: Boolean) {
        if (isSuccess) {
            showToast("Photo taken successfully")
        } else {
            showToast("Cannot take photo")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        App.resultObserver.removeListener(targetRoute)
        super.onDestroyView()
    }
}