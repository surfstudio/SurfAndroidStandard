package ru.surfstudio.android.build.model

import ru.surfstudio.android.build.EMPTY_STRING

/**
 * Represent information about component's samples
 */
data class Sample(
        val name: String = EMPTY_STRING,
        val dir: String = EMPTY_STRING
)