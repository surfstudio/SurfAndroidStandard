package ru.surfstudio.android.permission.sample.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.permission.sample.screen.main.camera.CameraPermissionRequest
import ru.surfstudio.android.permission.sample.screen.main.camera.CameraRoute
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.permission.sample.screen.main.location.LocationPermissionRequest
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
    basePresenterDependency: BasePresenterDependency,
    private val permissionManager: PermissionManager,
    private val activityNavigator: ActivityNavigator,
    private val messageController: MessageController
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun requestCameraPermission() {
        subscribe(permissionManager.request(CameraPermissionRequest())) { isGranted ->
            if (isGranted) {
                activityNavigator.start(CameraRoute())
            } else {
                messageController.show("Camera permission denied!")
            }
        }
    }

    fun requestLocationPermission() {
        subscribe(permissionManager.request(LocationPermissionRequest())) { isGranted ->
            messageController.show(if (isGranted) "Location granted" else "Location denied")
        }
    }
}