package ru.surfstudio.android.build.tasks.check_cross_feature_route.data

open class KotlinClassWrapper(
        val packageName: String,
        val className: String,
        val baseClassName: String,
        val baseClassPackageName: String,
        val implementations: List<String>,
        val classBody: String
)