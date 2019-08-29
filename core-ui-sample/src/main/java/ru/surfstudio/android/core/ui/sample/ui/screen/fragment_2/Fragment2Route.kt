package ru.surfstudio.android.core.ui.sample.ui.screen.fragment_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentWithResultRoute

class Fragment2Route : FragmentWithResultRoute<String>() {
    override fun prepareBundle(): Bundle = Bundle.EMPTY
    override fun getFragmentClass(): Class<out Fragment> = Fragment2View::class.java
}