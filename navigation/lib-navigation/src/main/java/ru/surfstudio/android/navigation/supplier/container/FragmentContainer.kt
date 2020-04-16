package ru.surfstudio.android.navigation.supplier.container

import androidx.annotation.IdRes

interface FragmentContainer {
    val containerId: Int @IdRes get
}