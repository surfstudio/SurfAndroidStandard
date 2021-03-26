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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import io.reactivex.Completable
import io.reactivex.Single
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.core.ui.permission.exceptions.PermissionsRationalIsNotProvidedException
import ru.surfstudio.android.core.ui.permission.exceptions.SettingsRationalIsNotProvidedException
import ru.surfstudio.android.core.ui.permission.screens.default_permission_rational.DefaultPermissionRationalRoute
import ru.surfstudio.android.core.ui.permission.screens.settings_rational.DefaultSettingsRationalRoute
import ru.surfstudio.android.navigation.command.activity.Start
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.command.activity.RequestPermission
import ru.surfstudio.android.navigation.observer.command.activity.StartForResult
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.observer.route.PermissionRequestRoute
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.rx.extension.observeScreenResult
import java.io.Serializable

/**
 * Класс для проверки и запросов разрешений.
 */
open class PermissionManager(
    private val activityProvider: ActiveActivityHolder,
    private val commandExecutor: AppCommandExecutorWithResult,
    private val sharedPreferences: SharedPreferences,
    private val screenResultObserver: ScreenResultObserver
) {

    protected fun performPermissionRequest(permissionRequest: PermissionRequest): Single<Boolean> {
        val route = PermissionRequestRoute(permissionRequest.permissions)
        return screenResultObserver
            .observeScreenResult(route)
            .firstOrError()
            .doOnSubscribe { commandExecutor.execute(RequestPermission(route)) }
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
            !isPermissionRequestRequested(permissionRequest) -> PermissionStatus.NOT_REQUESTED
            isPermissionRequestLastGranted(permissionRequest) -> PermissionStatus.GRANTED_ONCE
            isPermissionRequestDenied(permissionRequest) -> PermissionStatus.DENIED
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
            .flatMap {
                performPermissionRequestByDialogOrSettings(
                    permissionRequest,
                    permissionRequestStatus
                )
            }
            .doOnSuccess {
                setPermissionRequestIsRequested(permissionRequest)
                setPermissionRequestIsGranted(permissionRequest, it)
            }
    }

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
        when {
            permissionStatus != PermissionStatus.DENIED_FOREVER ->
                performPermissionRequest(permissionRequest)
            permissionRequest.showSettingsRational -> performPermissionRequestBySettings(
                permissionRequest
            )
            else -> Single.just(false)
        }

    private fun setPermissionRequestIsRequested(permissionRequest: PermissionRequest) =
        permissionRequest
            .permissions
            .forEach { permission -> setPermissionIsRequested(permission) }

    private fun setPermissionRequestIsGranted(
        permissionRequest: PermissionRequest,
        isGranted: Boolean
    ) = permissionRequest
        .permissions
        .forEach { permission -> setPermissionIsGranted(permission, isGranted) }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            activityProvider.activity!!,
            permission
        ) == PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale(permissionRequest: PermissionRequest): Boolean =
        permissionRequest
            .permissions
            .any { permission -> shouldShowPermissionRationale(permission) }

    private fun isPermissionRequested(permission: String) =
        sharedPreferences.getBoolean(permission, false)

    private fun isPermissionRequestLastGranted(permissionRequest: PermissionRequest): Boolean =
        permissionRequest
            .permissions
            .all { permission -> isPermissionLastGranted(permission) }

    private fun isPermissionLastGranted(permission: String) =
        sharedPreferences.getBoolean(GRANTED_PREFIX + permission, false)

    private fun setPermissionIsGranted(permission: String, isGranted: Boolean) =
        sharedPreferences
            .edit()
            .putBoolean(GRANTED_PREFIX + permission, isGranted)
            .apply()

    private fun needToShowPermissionRequestRational(permissionRequest: PermissionRequest): Boolean =
        permissionRequest.showPermissionsRational && shouldShowRequestPermissionRationale(
            permissionRequest
        )

    private fun showPermissionRequestRational(permissionRequest: PermissionRequest): Completable {
        val customPermissionsRationalRoute = permissionRequest.permissionsRationalRoute
        val customPermissionsRationalStr = permissionRequest.permissionsRationalStr

        val permissionRationalRoute = when {
            customPermissionsRationalRoute != null -> {
                return startAndObserveReturnFromScreenCustom(customPermissionsRationalRoute)
            }
            customPermissionsRationalStr != null -> DefaultPermissionRationalRoute(
                customPermissionsRationalStr
            )
            else -> return Completable.error(PermissionsRationalIsNotProvidedException())
        }

        return startAndObserveReturnFromScreen(permissionRationalRoute)
    }

    private fun performPermissionRequestBySettings(permissionRequest: PermissionRequest): Single<Boolean> {
        val customSettingsRationalRoute = permissionRequest.settingsRationalRoute
        val customSettingsRationalStr = permissionRequest.settingsRationalStr

        val settingsRationalRoute = when {
            customSettingsRationalRoute != null -> {
                return startAndObserveReturnFromScreenCustom(customSettingsRationalRoute)
                    .toSingle { check(permissionRequest).isGranted }
            }
            customSettingsRationalStr != null -> DefaultSettingsRationalRoute(
                customSettingsRationalStr
            )
            else -> return Single.error(SettingsRationalIsNotProvidedException())
        }
        return startAndObserveReturnFromScreen(settingsRationalRoute)
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
        ActivityCompat.shouldShowRequestPermissionRationale(activityProvider.activity!!, permission)

    private fun startAndObserveReturnFromScreenCustom(route: ActivityRoute): Completable {
        return screenResultObserver
            .observeScreenResult(route as ResultRoute<Serializable>)
            .firstElement()
            .flatMapCompletable { Completable.complete() }
            .doOnSubscribe { commandExecutor.execute(Start(route)) }
    }

    private fun startAndObserveReturnFromScreen(route: ActivityWithResultRoute<Serializable>): Completable =
        screenResultObserver
            .observeScreenResult(route)
            .firstElement()
            .flatMapCompletable { Completable.complete() }
            .doOnSubscribe { commandExecutor.execute(StartForResult(route)) }

    private companion object {
        const val GRANTED_PREFIX = "granted"
    }
}
