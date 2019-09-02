package ru.surfstudio.android.core.ui.sample.ui.screen.result_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentWithResultRoute

class ResultFragmentRoute : FragmentWithResultRoute<String>() {
    override fun prepareBundle(): Bundle = Bundle.EMPTY
    override fun getFragmentClass(): Class<out Fragment> = ResultFragmentView::class.java
}