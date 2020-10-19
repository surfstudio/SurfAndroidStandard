package ru.surfstudio.android.build.model.dependency

import org.gradle.api.GradleException

/**
 * Dependencies types
 */
enum class DependencyType(
        val gradleType: String,
        val mavenType: String,
        val isTransitive: Boolean
) {

    IMPLEMENTATION("implementation", "runtime", false),
    API("api", "compile", true),
    COMPILE_ONLY("compileOnly", "provided", false),
    RUNTIME_ONLY("runtimeOnly", "runtime", false),
    TEST_IMPLEMENTATION("testImplementation", "test", false),
    ANDROID_TEST_IMPLEMENTATION("androidTestImplementation", "test", false),
    KAPT("kapt", "", true),
    KAPT_TEST("kaptTest", "", true);

    companion object {

        fun fomString(type: String) = values().find { it.gradleType == type }
                ?: throw GradleException("failed to parse the type of dependency: $type")
    }
}