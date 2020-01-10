package ru.surfstudio.android.build.model.json

import ru.surfstudio.android.build.model.module.Sample
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.Transformable

/**
 * Represent information about component's samples json object
 */
data class SampleJson(
        val name: String = EMPTY_STRING,
        val dir: String = EMPTY_STRING
) : Transformable<Sample> {

    constructor(sample: Sample) : this(
            name = sample.name,
            dir = sample.directory
    )

    override fun transform() = Sample(name = name, directoryPath = dir)
}