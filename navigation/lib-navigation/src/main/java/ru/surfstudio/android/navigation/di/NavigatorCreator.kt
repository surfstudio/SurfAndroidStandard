package ru.surfstudio.android.navigation.di

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface

typealias FragmentNavigatorCreator =
        (fm: FragmentManager, containerId: Int, savedState: Bundle?) -> FragmentNavigatorInterface