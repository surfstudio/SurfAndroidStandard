package ru.surfstudio.android.navigation.navigator.fragment.container

import androidx.annotation.LayoutRes

interface FragmentContainer {
    val containerId: Int @LayoutRes get
}