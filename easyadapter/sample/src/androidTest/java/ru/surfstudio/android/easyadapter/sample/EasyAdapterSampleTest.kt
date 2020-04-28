package ru.surfstudio.android.easyadapter.sample

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import org.junit.Test
import ru.surfstudio.android.easyadapter.sample.ui.screen.async.AsyncInflateListActivityView
import ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff.AsyncDiffActivityView
import ru.surfstudio.android.easyadapter.sample.ui.screen.main.MainActivityView
import ru.surfstudio.android.easyadapter.sample.ui.screen.multitype.MultitypeListActivityView
import ru.surfstudio.android.easyadapter.sample.ui.screen.pagination.PaginationListActivityView
import ru.surfstudio.android.sample.common.test.base.BaseSampleTest
import ru.surfstudio.android.sample.common.test.utils.ActivityUtils.checkIfActivityIsVisible
import ru.surfstudio.android.sample.common.test.utils.RecyclerViewUtils.performItemClick
import ru.surfstudio.android.sample.common.test.utils.ViewUtils.performClick
import ru.surfstudio.android.sample.common.test.utils.VisibilityUtils.checkIfToastIsVisible

class EasyAdapterSampleTest : BaseSampleTest<MainActivityView>(MainActivityView::class.java) {

    @Test
    fun testEasyAdapterSample() {
        test(
                buttonResId = R.id.show_multitype_list_btn,
                activityClass = MultitypeListActivityView::class.java,
                lambda = {
                    performItemClick(R.id.rvMultitypeList, 0)
                    checkIfToastIsVisible("Value = 0")
                }
        )
        test(R.id.show_simple_paginationable_list_btn, PaginationListActivityView::class.java)
        test(R.id.show_async_inflate_list_btn, AsyncInflateListActivityView::class.java)
        test(R.id.show_async_diff_list_btn, AsyncDiffActivityView::class.java)
    }

    private fun <T> test(
            @IdRes buttonResId: Int,
            activityClass: Class<T>,
            lambda: () -> Unit = {}
    ) {
        performClick(buttonResId)
        checkIfActivityIsVisible(activityClass)
        lambda()
        Espresso.pressBack()
    }
}