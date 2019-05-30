package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING
import ru.surfstudio.android.build.model.json.JsonSample

/**
 * Represent information about component's samples
 */
class Sample(
        override val name: String = EMPTY_STRING,
        override val directory: String = EMPTY_STRING
) : Module(name, directory) {

    companion object {

        fun create(jsonSample: JsonSample) = Sample(
                name = jsonSample.name,
                directory = jsonSample.dir
        )
    }
}