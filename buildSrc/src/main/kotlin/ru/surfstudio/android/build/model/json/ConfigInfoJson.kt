package ru.surfstudio.android.build.model.json

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.model.ConfigInfo
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
) : Transformable<ConfigInfo> {

    constructor(configInfo: ConfigInfo) : this(
            configInfo.commonVersion,
            configInfo.isStable,
            configInfo.unstableVersion,
            configInfo.projectSnapshotName,
            configInfo.projectSnapshotVersion
    )

    override fun transform() = ConfigInfo(
            version,
            stable,
            unstableVersion,
            projectSnapshotName,
            projectSnapshotVersion
    )
}