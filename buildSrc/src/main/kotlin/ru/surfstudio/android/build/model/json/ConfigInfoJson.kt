package ru.surfstudio.android.build.model.json

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.model.GlobalConfigInfo
import ru.surfstudio.android.build.utils.Transformable

/**
 * Configuration information json
 */
data class ConfigInfoJson(
        val version: String,
        val stable: Boolean,
        @SerializedName("unstable_version") val unstableVersion: Int,
        @SerializedName("project_snapshot_name") val projectSnapshotName: String,
        @SerializedName("project_snapshot_version") val projectSnapshotVersion: Int
) : Transformable<GlobalConfigInfo> {

    constructor(globalConfigInfo: GlobalConfigInfo) : this(
            globalConfigInfo.commonVersion,
            globalConfigInfo.isStable,
            globalConfigInfo.unstableVersion,
            globalConfigInfo.projectSnapshotName,
            globalConfigInfo.projectSnapshotVersion
    )

    override fun transform() = GlobalConfigInfo(
            version,
            stable,
            unstableVersion,
            projectSnapshotName,
            projectSnapshotVersion
    )
}