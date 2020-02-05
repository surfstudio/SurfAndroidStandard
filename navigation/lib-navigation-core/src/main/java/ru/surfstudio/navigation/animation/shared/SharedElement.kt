package ru.surfstudio.navigation.animation.shared

import android.view.View
import java.io.Serializable

/**
 * Element that contains info to start SharedElementTransition
 *
 * @param sharedView view that will be shared between screens
 * @param transitionName unique transition name for this view
 */
data class SharedElement(
        val sharedView: View,
        val transitionName: String
) : Serializable
