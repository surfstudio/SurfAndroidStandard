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

/**
 * Класс для проверки и запросов разрешений.
 */
abstract class PermissionManager(
        eventDelegateManager: ScreenEventDelegateManager,
        private val activityProvider: ActivityProvider
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
        singleEmitterPerRequestCode[requestCode]?.onSuccess(isAllResultsAreGranted(grantResults))
        return true
    }

    /**
     * Проверить наличие всех разрешений в [PermissionRequest].
     *
     * @param permissionRequest проверяемый [PermissionRequest].
     *
     * @return true, если все разрешения выданы, false - если хотя бы одно не выдано.
     */
    fun check(permissionRequest: PermissionRequest): PermissionStatus =
            when {
                isPermissionRequestGranted(permissionRequest) -> PermissionStatus.GRANTED
                isPermissionRequestDenied(permissionRequest) -> PermissionStatus.DENIED
                !isPermissionRequestRequested(permissionRequest) -> PermissionStatus.NOT_REQUESTED
                else -> PermissionStatus.DENIED_FOREVER
            }




    private fun isPermissionRequestGranted(permissionRequest: PermissionRequest): Boolean {
        return permissionRequest.permissions
                .all { permission -> isPermissionGranted(permission) }
    }

    private fun isPermissionRequestRequested(permissionRequest: PermissionRequest): Boolean {

    }

    private fun isPermissionRequestDenied(permissionRequest: PermissionRequest): Boolean {
        shouldShowRequestPermissionRationale(permissionRequest)
    }



    /**
     * Запросить разрешение.
     *
     * @param permissionRequest выполняемый [PermissionRequest].
     *
     * @return [Single], содержащий [Boolean]: true, если разрешение выдано, false - если нет.
     */
    fun request(permissionRequest: PermissionRequest): Single<Boolean> {
        val permissionRequestStatus = check(permissionRequest)

        if (permissionRequest == PermissionStatus.GRANTED)
            return Single.just(true)


        val rationalCompletable = if (permissionRequest == PermissionStatus.NOT_REQUESTED) { //или если надо показывать
            Completable.completed()
        } else {
            showPermissionRequestRational(permissionRequest)
        }


        return rationalCompletable
                .toSingle()

//                .toSingle { false }
//                .flatMap {
//                        Single.create<Boolean> { singleEmitter ->
//                            singleEmitterPerRequestCode[permissionRequest.requestCode] = singleEmitter
//                            performPermissionRequest(permissionRequest)
//                        }
//                            .doOnDispose { singleEmitterPerRequestCode.remove(permissionRequest.requestCode) }
//                }



    }



    private fun showPermissionRequestRational(permissionRequest: PermissionRequest): Completable {
        if (permissionRequest.permissionRationalRoute != null) {
            //запустить роут
        } else {
            //запустить стандартный роут
        }
    }




    fun needToShowPermissionRequestRational(): Boolean {
//TODO:            permissionRequest.showPermissionRational &&

    }

    /**
     * Проверить, следует ли показывать пользователю объяснение, для чего нужен запрашиваемый [PermissionRequest].
     * Более подробно {@link #shouldShowPermissionRationale(String)}.
     *
     * @param permissionRequest проверяемый [PermissionRequest].
     *
     * @return true, если хотя бы для одного разрешения следует показать объяснение, false - если ни для одного
     * разрешения не следует показать объяснение.
     */
    fun shouldShowRequestPermissionRationale(permissionRequest: PermissionRequest): Boolean =
                    permissionRequest.permissions
                    .any { permission -> shouldShowPermissionRationale(permission) }






    private fun isAllResultsAreGranted(grantResults: IntArray): Boolean =
            grantResults
                    .all { grantResult -> grantResult == PERMISSION_GRANTED }

    private fun isPermissionGranted(permission: String): Boolean =
            ContextCompat.checkSelfPermission(activityProvider.get(), permission) == PERMISSION_GRANTED

    /**
     * Проверить, следует ли показывать пользователю объяснение, для чего нужен запрашиваемый Permission.
     *
     * @param permission проверяемое разрешение.
     *
     * @return true, если во время предыдущего запроса разрешения пользователь нажал отказ и не выбрал опцию "Don't ask
     * again", false - если разрешение запрашивается в первый раз, или если во время предыдущего запроса разрешения
     * пользователь выбрал опцию "Don't ask again".
     */
    private fun shouldShowPermissionRationale(permission: String) =
            ActivityCompat.shouldShowRequestPermissionRationale(activityProvider.get(), permission)
}