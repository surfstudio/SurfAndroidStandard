package ru.surfstudio.android.navigation.animation.shared

import android.view.View
import java.io.Serializable

/**
 * Element that contains data to start SharedElementTransition
 *
 * @param sharedView view that will be shared between screens
 * @param transitionName unique transition name for this view
 */
data class SharedElement(
        val sharedView: View,
        val transitionName: String
) : Serializable
