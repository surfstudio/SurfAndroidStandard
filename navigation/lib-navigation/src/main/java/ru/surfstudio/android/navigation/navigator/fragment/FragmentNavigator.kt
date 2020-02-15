package ru.surfstudio.android.navigation.navigator.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import java.lang.IllegalStateException

class FragmentNavigator() : BaseFragmentNavigator() {


    constructor(fragmentManager: FragmentManager) : this() {
        this.fragmentManagerInner = fragmentManager
    }

    constructor(fragmentManager: FragmentManager, containerId: Int) : this(fragmentManager) {
        this.containerId = containerId
    }

    constructor(
            fragmentManager: FragmentManager,
            containerId: Int,
            savedState: Bundle?
    ) : this(fragmentManager, containerId) {
        onRestoreState(savedState)
    }

    internal var fragmentManagerInner: FragmentManager? = null
    override val fragmentManager: FragmentManager
        get() = fragmentManagerInner ?: throw IllegalStateException()
    override var containerId: Int = super.containerId
}