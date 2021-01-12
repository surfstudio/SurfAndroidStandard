package ru.surfstudio.android.navigation.animation.utils

import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.styled.StyledAnimations

/**
 * Supplier which is responsible for inflating DialogFragments with animations.
 */
open class DialogAnimationSupplier {

    /**
     * Supplies animations to [DialogFragment]
     *
     * @param dialog dialog which animates
     * @param animations animations object, only [StyledAnimations] support
     */
    fun supplyWithAnimations(dialog: DialogFragment, animations: Animations) {
        if (animations is StyledAnimations) {
            setResourceAnimations(dialog, animations)
        }
    }

    /**
     * Sets windowAnimation to [DialogFragment]'s dialog
     */
    private fun setResourceAnimations(dialogFragment: DialogFragment, animations: StyledAnimations) {
        dialogFragment.dialog?.window?.setWindowAnimations(animations.style)
    }
}