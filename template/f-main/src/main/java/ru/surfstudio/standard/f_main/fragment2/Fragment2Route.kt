package ru.surfstudio.standard.f_main.fragment2

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentWithResultRoute

class Fragment2Route : FragmentWithResultRoute<String>() {

    override fun prepareBundle(): Bundle = Bundle()

    override fun getFragmentClass(): Class<out Fragment> = Fragment2View::class.java
}