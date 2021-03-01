package ru.surfstudio.android.navigation.sample.app.screen.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.command.fragment.ReplaceHard
import ru.surfstudio.android.navigation.observer.command.activity.StartForResult
import ru.surfstudio.android.navigation.sample.R
import ru.surfstudio.android.navigation.sample.app.App
import ru.surfstudio.android.navigation.sample.app.screen.auth.AuthRoute
import ru.surfstudio.android.navigation.sample.app.screen.flow.FlowRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.gallery.image.ImageRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.profile.about.AboutRoute
import ru.surfstudio.android.navigation.sample.app.screen.main.profile.settings.ApplicationSettingsRoute
import ru.surfstudio.android.navigation.sample.app.screen.number.NumberRoute
import ru.surfstudio.android.navigation.sample.app.screen.system.CameraHelper
import ru.surfstudio.android.navigation.sample.app.screen.system.CameraRoute
import ru.surfstudio.android.navigation.sample.app.utils.animations.FadeAnimations
import ru.surfstudio.android.navigation.sample.app.utils.animations.ModalAnimations
import ru.surfstudio.android.navigation.sample.app.utils.animations.SlideAnimations

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
        profile_logout_btn.setOnClickListener { App.executor.execute(ReplaceHard(AuthRoute(), FadeAnimations)) }
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
        profile_complex_chain_btn.setOnClickListener { executeComplexCommandChain() }

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

    private fun executeComplexCommandChain() {
        val firstFlow = FlowRoute("First flow")
        val secondFlow = FlowRoute("Second flow")
        App.executor.execute(
                listOf(
                        Start(NumberRoute(42)),
                        Start(firstFlow, animations = ModalAnimations),
                        Replace(ImageRoute(0), sourceTag = firstFlow.getId(), animations = FadeAnimations),
                        Replace(ImageRoute(1), sourceTag = firstFlow.getId(), animations = SlideAnimations),
                        Replace(ImageRoute(2), sourceTag = firstFlow.getId(), animations = SlideAnimations),
                        Start(NumberRoute(0), animations = ModalAnimations),
                        Start(secondFlow, animations = ModalAnimations),
                        Replace(ImageRoute(0), sourceTag = secondFlow.getId(), animations = FadeAnimations),
                        Replace(ImageRoute(1), sourceTag = secondFlow.getId(), animations = SlideAnimations),
                        Replace(ImageRoute(2), sourceTag = secondFlow.getId(), animations = SlideAnimations),
                        Start(NumberRoute(1), animations = ModalAnimations)
                )
        )
    }

    override fun onDestroyView() {
        App.resultObserver.removeListener(targetRoute)
        super.onDestroyView()
    }
}