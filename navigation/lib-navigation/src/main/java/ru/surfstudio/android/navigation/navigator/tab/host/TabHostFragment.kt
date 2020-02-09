package ru.surfstudio.android.navigation.navigator.tab.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Фрагмент, внутри которого лежит навигатор и стек одного таба
 */
class TabHostFragment : Fragment() {

    internal lateinit var navigator: FragmentNavigator

    private var route: FragmentRoute? = null
    private var containerId = 0
    private val hasNoRoot
        get() = navigator.isBackStackEmpty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = FragmentNavigator(childFragmentManager, containerId)
        parseArguments(arguments ?: return)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            FrameLayout(inflater.context).apply { id = containerId }

    override fun onResume() {
        super.onResume()
        route?.let { if (hasNoRoot) navigator.replace(it, EmptyScreenAnimations) }
    }

    private fun parseArguments(args: Bundle) {
        containerId = args.getInt(EXTRA_CONTAINER_ID)
    }

    companion object {

        /**
         * Create new [TabHostFragment] instance
         *
         * @param route root fragment tab, which will be shown when host will be displayed
         * @param containerId id of a ViewGroup, which will hold this host fragment
         */
        fun newInstance(
                route: FragmentRoute,
                containerId: Int = View.generateViewId()
        ) = TabHostFragment().apply {
            this.route = route
            arguments = Bundle().apply { putInt(EXTRA_CONTAINER_ID, containerId) }
        }

        const val EXTRA_CONTAINER_ID = "container_id"
    }
}
