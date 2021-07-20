package ru.surfstudio.android.core.ui.navigation.feature.installer

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Dynamic Feature install manager.
 */
class SplitFeatureInstaller(
        applicationContext: Context
) {
    private val splitFeatureInstallSubject = BehaviorSubject.create<SplitFeatureInstallState>()

    private val splitInstallManager = SplitInstallManagerFactory.create(applicationContext)

    //current installation session id
    private var sessionId = 0

    /**
     * Request Dynamic Feature installation.
     *
     * @param splitName Dynamic Feature name
     * @param splitFeatureInstallListener installation events listener
     */
    @Suppress("unused")
    fun installFeature(splitName: String) {
        installFeature(listOf(splitName))
    }

    /**
     * Request multiple Dynamic Features installation by one batch.
     *
     * @param splitNames multiple Dynamic Feature names
     * @param splitFeatureInstallListener installation events listener
     */
    fun installFeature(splitNames: List<String>): Observable<SplitFeatureInstallState> {
        val requestBuilder = SplitInstallRequest.newBuilder()
        for (splitName in splitNames) {
            requestBuilder.addModule(splitName)
        }
        val request = requestBuilder.build()

        this.splitInstallManager.registerListener { state ->
            if (state?.sessionId() == -1 &&
                    state.status() == SplitInstallSessionStatus.FAILED &&
                    state.errorCode() == SplitInstallErrorCode.SERVICE_DIED) {
                splitFeatureInstallSubject.onNext(
                        SplitFeatureInstallState(SplitFeatureEvent.StartupFailure.ServiceDied)
                )
            } else if (state?.sessionId() == this@SplitFeatureInstaller.sessionId) {
                when {
                    state.status() == SplitInstallSessionStatus.PENDING ->
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Pending
                                )
                        )
                    state.status() == SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION ->
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.RequiresUserConfirmation
                                )
                        )
                    state.status() == SplitInstallSessionStatus.DOWNLOADING ->
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Downloading(
                                                state.totalBytesToDownload(),
                                                state.bytesDownloaded()
                                        )
                                )
                        )
                    state.status() == SplitInstallSessionStatus.DOWNLOADED ->
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Downloaded
                                )
                        )
                    state.status() == SplitInstallSessionStatus.INSTALLING ->
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Installing
                                )
                        )
                    state.status() == SplitInstallSessionStatus.INSTALLED -> {
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Installed
                                )
                        )
                    }
                    state.status() == SplitInstallSessionStatus.FAILED -> {
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Failed
                                )
                        )
                    }
                    state.status() == SplitInstallSessionStatus.CANCELING ->
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Cancelling
                                )
                        )
                    state.status() == SplitInstallSessionStatus.CANCELED -> {
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Canceled
                                )
                        )
                    }
                    else -> {
                        splitFeatureInstallSubject.onNext(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.UnknownEvent(
                                                state.status()
                                        )
                                )
                        )
                    }
                }
            } else {
                splitFeatureInstallSubject.onNext(
                        SplitFeatureInstallState(
                                SplitFeatureEvent.InstallationStateEvent.AlienSessionEvent(
                                        state?.sessionId() ?: -1
                                )
                        )
                )
            }
        }
        this.splitInstallManager
                .startInstall(request)
                .addOnSuccessListener { sessionId ->
                    this@SplitFeatureInstaller.sessionId = sessionId ?: -1
                    splitFeatureInstallSubject.onNext(
                            SplitFeatureInstallState(
                                    SplitFeatureEvent.StartupSuccess
                            )
                    )
                }
                .addOnFailureListener { exception ->
                    if (exception is SplitInstallException) {
                        splitFeatureInstallSubject.onNext(
                                when {
                                    exception.errorCode == SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.ActiveSessionsLimitExceeded
                                        )
                                    exception.errorCode == SplitInstallErrorCode.MODULE_UNAVAILABLE ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.ModuleUnavailable
                                        )
                                    exception.errorCode == SplitInstallErrorCode.INVALID_REQUEST ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.InvalidRequest
                                        )
                                    exception.errorCode == SplitInstallErrorCode.SESSION_NOT_FOUND ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.SessionNotFound
                                        )
                                    exception.errorCode == SplitInstallErrorCode.API_NOT_AVAILABLE ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.ApiNotAvailable
                                        )
                                    exception.errorCode == SplitInstallErrorCode.ACCESS_DENIED ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.AccessDenied
                                        )
                                    exception.errorCode == SplitInstallErrorCode.NETWORK_ERROR ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.NetworkError
                                        )
                                    exception.errorCode == SplitInstallErrorCode.INCOMPATIBLE_WITH_EXISTING_SESSION ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.IncompatibleWithExistingSession
                                        )
                                    exception.errorCode == SplitInstallErrorCode.SERVICE_DIED ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.ServiceDied
                                        )
                                    else ->
                                        SplitFeatureInstallState(
                                                SplitFeatureEvent.StartupFailure.UnknownReason
                                        )
                                }
                        )
                    }
                }
        return splitFeatureInstallSubject
    }
}