package ru.surfstudio.android.core.ui.navigation.feature.installer

/**
 * Cross-feature startup event.
 *
 * @param installEvent Dynamic Feature installation event
 */
@Deprecated("Используйте новую навигацию")
data class SplitFeatureInstallState(
        var installEvent: SplitFeatureEvent? = null
) {

    constructor(installStatus: SplitFeatureInstallStatus) : this() {
        this.installStatus = installStatus
    }

    //cross-feature startup status
    @Suppress("MemberVisibilityCanBePrivate")
    var installStatus = SplitFeatureInstallStatus.getBySplitFeatureEvent(installEvent)
}