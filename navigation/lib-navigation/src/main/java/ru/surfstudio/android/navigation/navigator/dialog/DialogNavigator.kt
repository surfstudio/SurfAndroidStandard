package ru.surfstudio.android.navigation.navigator.dialog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.utils.DialogAnimationSupplier
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

class DialogNavigator(val activity: AppCompatActivity) : DialogNavigatorInterface {

    private var animationSupplier = DialogAnimationSupplier()

    override fun show(route: DialogRoute, animations: Animations) {
        val tag = route.getId()
        val dialog = route.createDialog()
        dialog.showNow(activity.supportFragmentManager, tag)
        animationSupplier.supplyWithAnimations(dialog, animations)
    }

    override fun dismiss(route: DialogRoute, animations: Animations) {
        val tag = route.getId()
        val dialog = activity.supportFragmentManager.findFragmentByTag(tag) as? DialogFragment
        dialog?.let { animationSupplier.supplyWithAnimations(it, animations) }
        dialog?.dismiss()
    }
}