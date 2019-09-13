package ru.surfstudio.android.build.model.module

import ru.surfstudio.android.build.utils.EMPTY_STRING

/**
 * Represent information about component's samples
 */
class Sample(
        override val name: String = EMPTY_STRING,
        override val directoryPath: String = EMPTY_STRING,
        val directory: String = EMPTY_STRING
) : Module(name, directoryPath)