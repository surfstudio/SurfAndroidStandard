package ru.surfstudio.android.build.model.json.bintray

import com.google.gson.annotations.SerializedName
import ru.surfstudio.android.build.utils.Transformable

data class BintrayPackageInfoJson(
        @SerializedName("name") val name: String
) : Transformable<String> {

    override fun transform() = name
}