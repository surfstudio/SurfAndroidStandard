package ru.surfstudio.android.navigation.di

import androidx.annotation.IdRes

interface FragmentContainer {
    val containerId: Int @IdRes get
}