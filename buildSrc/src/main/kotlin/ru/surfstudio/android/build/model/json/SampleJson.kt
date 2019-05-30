package ru.surfstudio.android.build.model.json

import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about component's samples json object
 */
data class SampleJson(
        val name: String = EMPTY_STRING,
        val dir: String = EMPTY_STRING
)