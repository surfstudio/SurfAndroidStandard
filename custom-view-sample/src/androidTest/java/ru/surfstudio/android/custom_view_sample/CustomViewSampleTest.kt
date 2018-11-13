package ru.surfstudio.android.custom_view_sample

import androidx.annotation.IdRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.launchActivity
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfViewIsNotVisible
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfViewIsVisible

@RunWith(AndroidJUnit4::class)
@SmallTest
class CustomViewSampleTest {

    @IdRes
    private val placeholderIdRes = R.id.placeholder_view

    @Before
    fun setUp() {
        launchActivity(MainActivity::class.java)
    }

    @Test
    fun testCustomView() {
        checkIfViewIsVisible(R.id.progress_bar_container, placeholderIdRes)
        checkIfViewIsNotVisible(R.id.placeholder_content_container, placeholderIdRes)
        // Смена состояния плейсхолдера
        performClick(R.id.change_state_btn)
    }
}