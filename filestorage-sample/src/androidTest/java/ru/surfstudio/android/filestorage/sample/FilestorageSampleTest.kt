package ru.surfstudio.android.filestorage.sample

import org.junit.Test
import ru.surfstudio.android.filestorage.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.sample.common.test.base.SimpleSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils

class FilestorageSampleTest : SimpleSampleTest<MainActivityView>(MainActivityView::class.java) {
    @Test
    fun testFilestorageSample() {
        ActivityUtils.launchActivity(MainActivityView::class.java)
    }
}