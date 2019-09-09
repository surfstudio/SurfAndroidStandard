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
package ru.surfstudio.android.core.ui.navigation.feature.installer

/**
 * All kinds of Dynamic Feature installation events.
 *
 * * installation startup success [StartupSuccess];
 * * installation status changing event (with subtypes) [InstallationStateEvent];
 * * installation startup failure (with subtypes) [StartupFailure];
 */
sealed class SplitFeatureEvent {

    /**
     * Installation startup success event.
     */
    object StartupSuccess : SplitFeatureEvent()

    /**
     * Event during installation process.
     */
    sealed class InstallationStateEvent : SplitFeatureEvent() {

        /**
         * The request has been accepted and the download should start soon.
         */
        object Pending : InstallationStateEvent()

        /**
         * The download requires user confirmation. This is most likely due to the size of the
         * download being larger than 10 MB.
         */
        object RequiresUserConfirmation : InstallationStateEvent()

        /**
         * Download is in progress.
         *
         * @param totalBytes total size of the downloading feature in bytes
         * @param progress current downloading progress
         */
        data class Downloading(val totalBytes: Long,
                               val progress: Long) : InstallationStateEvent()

        /**
         * The device has downloaded the module but installation has no yet begun.
         */
        object Downloaded : InstallationStateEvent()

        /**
         * The device is currently installing the module.
         */
        object Installing : InstallationStateEvent()

        /**
         * The module is installed on the device.
         */
        object Installed : InstallationStateEvent()

        /**
         * The request failed before the module was installed on the device.
         */
        object Failed : InstallationStateEvent()

        /**
         * The device is in the process of cancelling the request.
         */
        object Cancelling : InstallationStateEvent()

        /**
         * The request has been cancelled.
         */
        object Canceled : InstallationStateEvent()

        /**
         * Event from another installation session.
         *
         * @param sessionId installation session ID
         */
        data class AlienSessionEvent(val sessionId: Int) : InstallationStateEvent()

        /**
         * Unknown event.
         *
         * @param status current status code
         */
        data class UnknownEvent(val status: Int) : InstallationStateEvent()
    }

    /**
     * Installation startup failure event.
     */
    sealed class StartupFailure : SplitFeatureEvent() {

        /**
         * The request is rejected because there is at least one existing request that is
         * currently downloading.
         */
        object ActiveSessionsLimitExceeded : StartupFailure()

        /**
         * Google Play is unable to find the requested module based on the current installed
         * version of the app, device, and user’s Google Play account.
         */
        object ModuleUnavailable : StartupFailure()

        /**
         * Google Play received the request, but the request is not valid.
         */
        object InvalidRequest : StartupFailure()

        /**
         * A session for a given session ID was not found.
         */
        object SessionNotFound : StartupFailure()

        /**
         * The Play Core Library is not supported on the current device. That is, the device is
         * not able to download and install features on demand.
         */
        object ApiNotAvailable : StartupFailure()

        /**
         * The app is unable to register the request because of insufficient permissions.
         */
        object AccessDenied : StartupFailure()

        /**
         * The request failed because of a network error.
         */
        object NetworkError : StartupFailure()

        /**
         * The request contains one or more modules that have already been requested but have
         * not yet been installed.
         */
        object IncompatibleWithExistingSession : StartupFailure()

        /**
         * The service responsible for handling the request has died.
         */
        object ServiceDied : StartupFailure()

        /**
         * UnknownEvent error.
         */
        object UnknownReason : StartupFailure()
    }

}