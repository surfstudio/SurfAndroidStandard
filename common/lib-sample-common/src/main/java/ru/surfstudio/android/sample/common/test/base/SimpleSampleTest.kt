package ru.surfstudio.android.sample.common.test.base

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils

/**
 * Базовый класс для простейших инструментальных тестов примеров,
 * тестирование которых заключается в простом запуске приложения и проверке, что она прошла успешно
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
open class SimpleSampleTest<T : Activity>(private val mainActivityClass: Class<T>) {

    @Test
    fun testSample() {
        ActivityUtils.launchActivity(mainActivityClass)
    }
}