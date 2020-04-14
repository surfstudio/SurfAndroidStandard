package ru.surfstudio.android.build.tasks.validate_cross_feature_routes.data

/**
 * Wrapper that contains parsed information of kotlin class file.
 * */
open class KClassWrapper(
        val packageName: String,
        val className: String,
        val baseClassPackageName: String,
        val baseClassName: String,
        val implementations: List<String>,
        val classBody: String
) {
    val fullName: String = "$packageName.$className"

    override fun toString(): String {
        return "$className (package $packageName)"
    }
}