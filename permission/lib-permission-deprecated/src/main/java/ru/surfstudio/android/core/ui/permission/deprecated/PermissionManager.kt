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
package ru.surfstudio.android.core.ui.permission.deprecated

import android.Manifest
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.result.RequestPermissionsResultDelegate
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.permission.deprecated.exceptions.PermissionsRationalIsNotProvidedException
import ru.surfstudio.android.core.ui.permission.deprecated.exceptions.SettingsRationalIsNotProvidedException
import ru.surfstudio.android.core.ui.permission.deprecated.screens.default_permission_rational.DefaultPermissionRationalRoute
import ru.surfstudio.android.core.ui.permission.deprecated.screens.settings_rational.DefaultSettingsRationalRoute
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import java.io.Serializable
import java.util.*

/**
 * Класс для проверки и запросов разрешений.
 */
@Deprecated("Prefer using new implementation")
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

    /**
     * Выполнить запрос разрешений.
     */
    protected abstract fun performPermissionRequest(permissionRequest: PermissionRequest)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        val nonNullSingleEmitter = singleEmitterPerRequestCode[requestCode] ?: return false
        if (nonNullSingleEmitter.isDisposed) return true

        val isPermissionRequestGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val locationPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (permissions.contentEquals(locationPermissions)) {
                isAnyResultGranted(grantResults)
            } else {
                isAllResultsAreGranted(grantResults)
            }
        } else {
            isAllResultsAreGranted(grantResults)
        }
        nonNullSingleEmitter.onSuccess(isPermissionRequestGranted)

        return true
    }

    /**
     * Проверить наличие всех разрешений в [PermissionRequest].
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     *
     * @return Статус разрешения [PermissionStatus].
     */
    fun check(permissionRequest: PermissionRequest): PermissionStatus = when {
        isPermissionRequestGranted(permissionRequest) -> PermissionStatus.GRANTED
        !isPermissionRequestRequested(permissionRequest) -> PermissionStatus.NOT_REQUESTED
        isPermissionRequestDenied(permissionRequest) -> PermissionStatus.DENIED
        else -> PermissionStatus.DENIED_FOREVER_OR_ONE_TIME_PERMISSION
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
                performPermissionRequestByDialogOrSettings(permissionRequest, permissionRequestStatus)
            }
            .doOnSuccess {
                setPermissionRequestIsRequested(permissionRequest)
            }
    }

    private fun isAllResultsAreGranted(grantResults: IntArray): Boolean =
        grantResults
            .all { grantResult -> grantResult == PERMISSION_GRANTED }

    private fun isAnyResultGranted(grantResults: IntArray): Boolean =
        grantResults
            .any { grantResult -> grantResult == PERMISSION_GRANTED }

    /**
     * Проверяем, выдал ли пользователь разрешения.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    private fun isPermissionRequestGranted(permissionRequest: PermissionRequest): Boolean {
        val requestPermissions = permissionRequest.permissions
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && requestPermissions.contentEquals(locationPermissions)) {
            permissionRequest.permissions.any { permission -> isPermissionGranted(permission) }
        } else {
            permissionRequest.permissions.all { permission -> isPermissionGranted(permission) }
        }
    }

    /**
     * Проверяем, отказал ли пользователь в выдаче разрешений.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    private fun isPermissionRequestDenied(permissionRequest: PermissionRequest): Boolean =
        shouldShowRequestPermissionRationale(permissionRequest)

    /**
     * Проверяем запрашивались ли уже разрешения.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    private fun isPermissionRequestRequested(permissionRequest: PermissionRequest): Boolean = permissionRequest
        .permissions
        .all { permission -> isPermissionRequested(permission) }

    /**
     * Отображает диалог, объясняющий, зачем требуется это разрешение, если нужно.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    private fun showPermissionRequestRationalIfNeeded(permissionRequest: PermissionRequest): Completable =
        if (needToShowPermissionRequestRational(permissionRequest)) {
            showPermissionRequestRational(permissionRequest)
        } else {
            Completable.complete()
        }

    /**
     * Если разрешение было выдано, то пользователь получит true, иначе, для версий Android до 11,
     * мы предлагаем пользователю перейти в настройки, на версии после 11, мы вызываем
     * tryOneTimePermissionRequest.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     * @param permissionStatus статус [PermissionRequest].
     */
    private fun performPermissionRequestByDialogOrSettings(
        permissionRequest: PermissionRequest,
        permissionStatus: PermissionStatus
    ): Single<Boolean> =
        when {
            permissionStatus != PermissionStatus.DENIED_FOREVER_OR_ONE_TIME_PERMISSION ->
                performPermissionRequestByDialog(permissionRequest)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> tryOneTimePermissionRequest(permissionRequest)
            permissionRequest.showSettingsRational -> performPermissionRequestBySettings(permissionRequest)
            else -> Single.just(false)
        }

    /**
     * Данный метод нужен для Android 11, из-за одноразовых разрешений флаги в случае, когда пользоатель
     * выбрал одноразовые разрешения или "Don't ask again" совпадают, поэтому на Android 11 мы вначале пытаемся запросить у системы
     * разрешение, если разрешение уже запрашивалось и пользователь отказал, то shouldShowRequestPermissionRationale
     * вернет true, это будет означать, что первый запрос уже был, пользователь отказал и мы пропускаем
     * дальше по цепочке, если shouldShowRequestPermissionRationale вернет false, то это означает, что
     * пользователь выбрал "Don't ask again" и надо предложить перейти ему в настройки.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun tryOneTimePermissionRequest(
        permissionRequest: PermissionRequest
    ): Single<Boolean> = performPermissionRequestByDialog(permissionRequest).flatMap { isGranted ->
        //Данное условие определит, если в настройках указанно, что у нас только одноразовое разрешение
        if (isGranted || (!isGranted && shouldShowRequestPermissionRationale(permissionRequest))) {
            Single.just(isGranted)
        } else {
            if (permissionRequest.showSettingsRational) {
                performPermissionRequestBySettings(permissionRequest)
            } else {
                Single.just(false)
            }
        }
    }

    /**
     * Установить флаг того, что у пользователя запрашивали разрешения.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    private fun setPermissionRequestIsRequested(permissionRequest: PermissionRequest) =
        permissionRequest
            .permissions
            .forEach { permission -> setPermissionIsRequested(permission) }

    /**
     * Проверить, давал ли пользователь разрешение.
     *
     * @return true, если пользователь дал разрешение, false, если пользователь не выдавал разрешение
     */
    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(activityProvider.get(), permission) == PERMISSION_GRANTED

    /**
     * Проверить, следует ли показывать пользователю объяснение, для чего нужны запрашиваемые Permissions.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     *
     * @return True, если во время предыдущего запроса разрешения пользователь нажал отказ и не выбрал опцию "Don't ask
     * again", false - если разрешение запрашивается в первый раз, или если во время предыдущего запроса разрешения
     * пользователь выбрал опцию "Don't ask again".
     */
    private fun shouldShowRequestPermissionRationale(permissionRequest: PermissionRequest): Boolean {
        return permissionRequest
            .permissions
            .any { permission -> shouldShowPermissionRationale(permission) }
    }

    /**
     * Проверяет запрашивали ли мы ранее это разрешение.
     *
     * @param permission Проверяемое разрешение.
     *
     * @return true если уже запрос по этому разрешению был, false если не было запроса.
     */
    private fun isPermissionRequested(permission: String) = sharedPreferences.getBoolean(permission, false)

    private fun needToShowPermissionRequestRational(permissionRequest: PermissionRequest): Boolean =
        permissionRequest.showPermissionsRational && shouldShowRequestPermissionRationale(permissionRequest)

    /**
     * Отображает диалог, объясняющий, зачем требуется это разрешение.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    private fun showPermissionRequestRational(permissionRequest: PermissionRequest): Completable {
        val customPermissionsRationalRoute = permissionRequest.permissionsRationalRoute
        val customPermissionsRationalStr = permissionRequest.permissionsRationalStr

        val permissionRationalRoute = when {
            customPermissionsRationalRoute != null -> customPermissionsRationalRoute
            customPermissionsRationalStr != null -> DefaultPermissionRationalRoute(customPermissionsRationalStr)
            else -> return Completable.error(PermissionsRationalIsNotProvidedException())
        }

        return startAndObserveReturnFromScreen(permissionRationalRoute)
    }

    /**
     * Отображает системный диалог запроса разрешения
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    private fun performPermissionRequestByDialog(permissionRequest: PermissionRequest): Single<Boolean> =
        Single
            .create<Boolean> { singleEmitter ->
                singleEmitterPerRequestCode[permissionRequest.requestCode] = singleEmitter
                performPermissionRequest(permissionRequest)
            }
            .doFinally { singleEmitterPerRequestCode.remove(permissionRequest.requestCode) }

    /**
     * Предлагаем пользователю перейти на экран настроек приложения для активации разрешения.
     *
     * @param permissionRequest Проверяемый [PermissionRequest].
     */
    private fun performPermissionRequestBySettings(permissionRequest: PermissionRequest): Single<Boolean> {
        val customSettingsRationalRoute = permissionRequest.settingsRationalRoute
        val customSettingsRationalStr = permissionRequest.settingsRationalStr

        val settingsRationalRoute = when {
            customSettingsRationalRoute != null -> customSettingsRationalRoute
            customSettingsRationalStr != null -> DefaultSettingsRationalRoute(customSettingsRationalStr)
            else -> return Single.error(SettingsRationalIsNotProvidedException())
        }
        return startAndObserveReturnFromScreen(settingsRationalRoute)
            .toSingle { check(permissionRequest).isGranted }
    }

    /**
     * Устанавливает флаг, что проверяемое разрешение запрошенно.
     *
     * @param permission Проверяемое разрешение.
     */
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

    private fun startAndObserveReturnFromScreen(route: ActivityWithResultRoute<*>): Completable =
        activityNavigator
            .observeResult<Serializable>(route)
            .firstElement()
            .flatMapCompletable { Completable.complete() }
            .doOnSubscribe { activityNavigator.startForResult(route) }
}