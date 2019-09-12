package ru.surfstudio.android.easyadapter.sample

import androidx.test.espresso.Espresso
import org.junit.Test
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
        performClick(R.id.show_multitype_list_btn)
        checkIfActivityIsVisible(MultitypeListActivityView::class.java)

        performItemClick(R.id.rvMultitypeList, 0)
        checkIfToastIsVisible("Value = 0")
        Espresso.pressBack()

        performClick(R.id.show_paginationable_list)
        checkIfActivityIsVisible(PaginationListActivityView::class.java)
    }
}