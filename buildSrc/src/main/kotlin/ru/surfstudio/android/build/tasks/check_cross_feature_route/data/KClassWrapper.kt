package ru.surfstudio.android.build.tasks.check_cross_feature_route.data

// TODO doc
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