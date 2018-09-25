/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.permission

import android.content.SharedPreferences
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

import java.util.HashMap

import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.result.RequestPermissionsResultDelegate
import ru.surfstudio.android.core.ui.provider.ActivityProvider

import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.permission.exceptions.PermissionsRationalIsNotProvidedException
import ru.surfstudio.android.core.ui.permission.exceptions.SettingsRationalIsNotProvidedException
import ru.surfstudio.android.core.ui.permission.screens.default_permission_rational.DefaultPermissionRationalRoute
import ru.surfstudio.android.core.ui.permission.screens.settings_rational.SettingsRationalRoute
import java.io.Serializable

/**
 * Класс для проверки и запросов разрешений.
 */
abstract class PermissionManager(
        eventDelegateManager: ScreenEventDelegateManager,
        private val activityProvider: ActivityProvider,
        private val activityNavigator: ActivityNavigator,
        private val sharedPreferences: SharedPreferences
) : RequestPermissionsResultDelegate {

    private val singleEmitterPerRequestCode = HashMap<Int, SingleEmitter<Boolean>>()

    init {
        eventDelegateManager.registerDelegate(this)
    }

    protected abstract fun performPermissionRequest(permissionRequest: PermissionRequest)

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ): Boolean {
        if (singleEmitterPerRequestCode.containsKey(requestCode)) {
            return false
        }

        val isPermissionRequestGranted = isAllResultsAreGranted(grantResults)
        singleEmitterPerRequestCode[requestCode]?.onSuccess(isPermissionRequestGranted)
        return true
    }

    /**
     * Проверить наличие всех разрешений в [PermissionRequest].
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     *
     * @return Статус разрешения [PermissionStatus].
     */
    fun check(permissionRequest: PermissionRequest): PermissionStatus =
            when {
                isPermissionRequestGranted(permissionRequest) -> PermissionStatus.GRANTED
                isPermissionRequestDenied(permissionRequest) -> PermissionStatus.DENIED
                !isPermissionRequestRequested(permissionRequest) -> PermissionStatus.NOT_REQUESTED
                else -> PermissionStatus.DENIED_FOREVER
            }

    /**
     * Запросить разрешение.
     *
     * @param permissionRequest Выполняемый [PermissionRequest].
     *
     * @return [Single], содержащий [Boolean]: true, если разрешение выдано, false - если нет.
     */
    fun request(permissionRequest: PermissionRequest): Single<Boolean> {
        val permissionRequestStatus = check(permissionRequest)

        if (permissionRequestStatus.isGranted) {
            return Single.just(true)
        }

        return showPermissionRequestRationalIfNeeded(permissionRequest)
                .toSingleDefault(false)
                .flatMap { performPermissionRequestByDialogOrSettings(permissionRequest, permissionRequestStatus) }
                .doOnSubscribe { setPermissionRequestIsRequested(permissionRequest) }
    }

    private fun isAllResultsAreGranted(grantResults: IntArray): Boolean =
            grantResults
                    .all { grantResult -> grantResult == PERMISSION_GRANTED }

    private fun isPermissionRequestGranted(permissionRequest: PermissionRequest): Boolean =
            permissionRequest
                    .permissions
                    .all { permission -> isPermissionGranted(permission) }

    private fun isPermissionRequestDenied(permissionRequest: PermissionRequest): Boolean =
            shouldShowRequestPermissionRationale(permissionRequest)

    private fun isPermissionRequestRequested(permissionRequest: PermissionRequest): Boolean =
            permissionRequest
                    .permissions
                    .all { permission -> isPermissionRequested(permission) }

    private fun showPermissionRequestRationalIfNeeded(permissionRequest: PermissionRequest): Completable =
            if (needToShowPermissionRequestRational(permissionRequest)) {
                showPermissionRequestRational(permissionRequest)
            } else {
                Completable.complete()
            }

    private fun performPermissionRequestByDialogOrSettings(
            permissionRequest: PermissionRequest,
            permissionStatus: PermissionStatus
    ): Single<Boolean> =
            if (permissionStatus != PermissionStatus.DENIED_FOREVER) {
                performPermissionRequestByDialog(permissionRequest)
            } else {
                performPermissionRequestBySettings(permissionRequest)
            }

    private fun setPermissionRequestIsRequested(permissionRequest: PermissionRequest) =
            permissionRequest
                    .permissions
                    .forEach { permission -> setPermissionIsRequested(permission) }

    private fun isPermissionGranted(permission: String): Boolean =
            ContextCompat.checkSelfPermission(activityProvider.get(), permission) == PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale(permissionRequest: PermissionRequest): Boolean =
            permissionRequest
                    .permissions
                    .any { permission -> shouldShowPermissionRationale(permission) }

    private fun isPermissionRequested(permission: String) = sharedPreferences.getBoolean(permission, false)

    private fun needToShowPermissionRequestRational(permissionRequest: PermissionRequest): Boolean =
            permissionRequest.showPermissionsRational && shouldShowRequestPermissionRationale(permissionRequest)

    private fun showPermissionRequestRational(permissionRequest: PermissionRequest): Completable {
        val permissionsRationalRoute = permissionRequest.permissionsRationalRoute
        val permissionsRationalStringRes = permissionRequest.permissionsRationalStr

        val rationalRoute = when {
            permissionsRationalRoute != null -> permissionsRationalRoute
            permissionsRationalStringRes!= null ->
                DefaultPermissionRationalRoute(permissionsRationalStringRes)
            else -> throw PermissionsRationalIsNotProvidedException()
        }

        return activityNavigator
                .observeResult<Serializable>(rationalRoute)
                .ignoreElements()
                .doOnSubscribe { activityNavigator.startForResult(rationalRoute) }
    }

    private fun performPermissionRequestByDialog(permissionRequest: PermissionRequest): Single<Boolean> {
        return Single
                .create<Boolean> { singleEmitter ->
                    singleEmitterPerRequestCode[permissionRequest.requestCode] = singleEmitter
                    performPermissionRequest(permissionRequest)
                }
                .doOnDispose { singleEmitterPerRequestCode.remove(permissionRequest.requestCode) }
    }

    private fun performPermissionRequestBySettings(permissionRequest: PermissionRequest): Single<Boolean> {
        val settingsRationalStr =
                permissionRequest.settingsRationalStr ?: throw SettingsRationalIsNotProvidedException()
        val settingsRationalRoute = SettingsRationalRoute(settingsRationalStr)

        return activityNavigator
                .observeResult<Serializable>(settingsRationalRoute)
                .ignoreElements()
                .doOnSubscribe { activityNavigator.startForResult(settingsRationalRoute) }
                .toSingle { check(permissionRequest).isGranted }
    }

    private fun setPermissionIsRequested(permission: String) =
            sharedPreferences
                    .edit()
                    .putBoolean(permission, true)
                    .apply()

    /**
     * Проверить, следует ли показывать пользователю объяснение, для чего нужен запрашиваемый Permission.
     *
     * @param permission Проверяемое разрешение.
     *
     * @return True, если во время предыдущего запроса разрешения пользователь нажал отказ и не выбрал опцию "Don't ask
     * again", false - если разрешение запрашивается в первый раз, или если во время предыдущего запроса разрешения
     * пользователь выбрал опцию "Don't ask again".
     */
    private fun shouldShowPermissionRationale(permission: String) =
            ActivityCompat.shouldShowRequestPermissionRationale(activityProvider.get(), permission)
}