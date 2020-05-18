package ru.surfstudio.android.core.ui.navigation.feature.installer

/**
 * Cross-feature startup status.
 *
 * [IN_PROGRESS] value is possible during navigation to Dynamic Feature only.
 */
enum class SplitFeatureInstallStatus {
    SUCCESS,
    IN_PROGRESS,
    FAILURE,
    UNKNOWN;

    companion object {

        fun getByValue(value: Boolean) =
                if (value) {
                    SUCCESS
                } else {
                    FAILURE
                }

        fun getBySplitFeatureEvent(splitFeatureEvent: SplitFeatureEvent?) =
                if (splitFeatureEvent == null) {
                    UNKNOWN
                } else if (splitFeatureEvent is SplitFeatureEvent.InstallationStateEvent.Installed) {
                    SUCCESS
                } else if (splitFeatureEvent is SplitFeatureEvent.InstallationStateEvent.Failed ||
                        splitFeatureEvent is SplitFeatureEvent.InstallationStateEvent.Canceled ||
                        splitFeatureEvent is SplitFeatureEvent.StartupFailure) {
                    FAILURE
                } else {
                    IN_PROGRESS
                }
    }
}