package ru.surfstudio.android.core.ui.navigation.feature.installer

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

/**
 * Dynamic Feature install manager.
 */
class SplitFeatureInstaller(
        applicationContext: Context
) {

    interface SplitFeatureInstallListener {
        fun onInstall(state: SplitFeatureInstallState)
        fun onStateChanged(state: SplitFeatureInstallState)
        fun onFailure(state: SplitFeatureInstallState)
    }

    private val splitInstallManager = SplitInstallManagerFactory.create(applicationContext)

    //current installation session id
    private var sessionId = 0

    /**
     * Request Dynamic Feature installation.
     *
     * @param splitName Dynamic Feature name
     * @param splitFeatureInstallListener installation events listener
     */
    fun installFeature(
            splitName: String,
            splitFeatureInstallListener: SplitFeatureInstallListener
    ) {
        installFeature(listOf(splitName), splitFeatureInstallListener)
    }

    /**
     * Request multiple Dynamic Features installation by one batch.
     *
     * @param splitNames multiple Dynamic Feature names
     * @param splitFeatureInstallListener installation events listener
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun installFeature(
            splitNames: List<String>,
            splitFeatureInstallListener: SplitFeatureInstallListener
    ) {
        val requestBuilder = SplitInstallRequest.newBuilder()
        for (splitName in splitNames) {
            requestBuilder.addModule(splitName)
        }
        val request = requestBuilder.build()

        this.splitInstallManager.registerListener { state ->
            if (state?.sessionId() == -1 &&
                    state.status() == SplitInstallSessionStatus.FAILED &&
                    state.errorCode() == SplitInstallErrorCode.SERVICE_DIED) {
                splitFeatureInstallListener.onFailure(
                        SplitFeatureInstallState(SplitFeatureEvent.StartupFailure.ServiceDied)
                )
            } else if (state?.sessionId() == this@SplitFeatureInstaller.sessionId) {
                when {
                    state.status() == SplitInstallSessionStatus.PENDING ->
                        splitFeatureInstallListener.onStateChanged(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Pending
                                )
                        )
                    state.status() == SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION ->
                        splitFeatureInstallListener.onStateChanged(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.RequiresUserConfirmation
                                )
                        )
                    state.status() == SplitInstallSessionStatus.DOWNLOADING ->
                        splitFeatureInstallListener.onStateChanged(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Downloading(
                                                state.totalBytesToDownload(),
                                                state.bytesDownloaded()
                                        )
                                )
                        )
                    state.status() == SplitInstallSessionStatus.DOWNLOADED ->
                        splitFeatureInstallListener.onStateChanged(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Downloaded
                                )
                        )
                    state.status() == SplitInstallSessionStatus.INSTALLING ->
                        splitFeatureInstallListener.onStateChanged(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Installing
                                )
                        )
                    state.status() == SplitInstallSessionStatus.INSTALLED -> {
                        splitFeatureInstallListener.onInstall(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Installed
                                )
                        )
                    }
                    state.status() == SplitInstallSessionStatus.FAILED -> {
                        splitFeatureInstallListener.onFailure(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Failed
                                )
                        )
                    }
                    state.status() == SplitInstallSessionStatus.CANCELING ->
                        splitFeatureInstallListener.onStateChanged(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Cancelling
                                )
                        )
                    state.status() == SplitInstallSessionStatus.CANCELED -> {
                        splitFeatureInstallListener.onFailure(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.Canceled
                                )
                        )
                    }
                    else -> {
                        splitFeatureInstallListener.onFailure(
                                SplitFeatureInstallState(
                                        SplitFeatureEvent.InstallationStateEvent.UnknownEvent(
                                                state.status()
                                        )
                                )
                        )
                    }
                }
            } else {
                splitFeatureInstallListener.onStateChanged(
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
                    splitFeatureInstallListener.onStateChanged(
                            SplitFeatureInstallState(
                                    SplitFeatureEvent.StartupSuccess
                            )
                    )
                }
                .addOnFailureListener { exception ->
                    if (exception is SplitInstallException) {
                        splitFeatureInstallListener.onFailure(
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
    }
}