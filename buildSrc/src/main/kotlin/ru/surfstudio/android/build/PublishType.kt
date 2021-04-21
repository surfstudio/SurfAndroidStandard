package ru.surfstudio.android.build

import ru.surfstudio.android.build.artifactory.ArtifactoryConfig
import ru.surfstudio.android.build.maven.MavenConfig
import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Defines a type of url for deploy
 */
enum class PublishType(val id: String) {
    ARTIFACTORY("artifactory"),
    MAVEN_RELEASE("maven_release"),
    UNKNOWN("unknown");

    companion object {
        @JvmStatic
        fun getById(id: String): PublishType {
            return values().find { it.id == id } ?: UNKNOWN
        }
    }
}

object PublishUtil {

    @JvmStatic
    fun getPublishData(data: PublishType): PublishData = when (data) {
        PublishType.ARTIFACTORY -> PublishData(
            url = ArtifactoryConfig.DEPLOY_URL,
            userName = ArtifactoryConfig.USER_NAME,
            password = ArtifactoryConfig.PASSWORD
        )
        PublishType.MAVEN_RELEASE -> PublishData(
            url = MavenConfig.RELEASE_DEPLOY_URL,
            userName = MavenConfig.USER_NAME,
            password = MavenConfig.PASSWORD
        )
        PublishType.UNKNOWN -> PublishData()
    }
}

data class PublishData(
    val url: String = EMPTY_STRING,
    val userName: String = EMPTY_STRING,
    val password: String = EMPTY_STRING
)