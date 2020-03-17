package ru.surfstudio.android.build

import com.google.gson.GsonBuilder
import ru.surfstudio.android.build.model.GlobalConfigInfo
import ru.surfstudio.android.build.model.json.ConfigInfoJson
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.JsonHelper
import java.io.File

private const val CONFIG_INFO_JSON_FILE_PATH = "buildSrc/projectConfiguration.json"

/**
 * Class to work with project configInfo
 */
object ConfigInfoProvider {

    private var configInfoJsonFile: File = openConfigInfoFile()

    lateinit var globalConfigInfo: GlobalConfigInfo

    /**
     * specified only when running settings for ability to add android standard locally
     * otherwise will be empty
     */
    var currentDirectory = EMPTY_STRING
        set(value) {
            field = value
            configInfoJsonFile = openConfigInfoFile()
            globalConfigInfo = parseProjectConfigInfoJson()
        }

    fun incrementUnstableVersion() {
        val newCounter = globalConfigInfo.unstableVersion + 1
        println("New global version unstable counter: $newCounter")
        val newConfigInfo = globalConfigInfo.copy(unstableVersion = newCounter)
        JsonHelper.write(ConfigInfoJson(newConfigInfo), configInfoJsonFile)
        this.globalConfigInfo = newConfigInfo //fix reuse process with old config info for next tasks
    }

    fun incrementProjectSnapshotVersion() {
        val newCounter = globalConfigInfo.projectSnapshotVersion + 1
        println("New project snapshot version counter: $newCounter")
        val newConfigInfo = globalConfigInfo.copy(projectSnapshotVersion = newCounter)
        JsonHelper.write(ConfigInfoJson(newConfigInfo), configInfoJsonFile)
        this.globalConfigInfo = newConfigInfo //fix reuse process with old config info for next tasks
    }

    private fun parseProjectConfigInfoJson(): GlobalConfigInfo {
        return GsonBuilder().create()
                .fromJson(configInfoJsonFile.reader(), ConfigInfoJson::class.java)
                .transform()
    }

    private fun openConfigInfoFile() = File(currentDirectory + CONFIG_INFO_JSON_FILE_PATH)

    @JvmStatic
    private fun getVersion() = globalConfigInfo.version
}