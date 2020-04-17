package ru.surfstudio.android.navigation.provider.container

import androidx.annotation.IdRes

interface FragmentNavigationContainer {
    val containerId: Int @IdRes get
}