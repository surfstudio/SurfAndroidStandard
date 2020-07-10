package ru.surfstudio.android.navigation.navigator.dialog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

class DialogNavigator(val activity: AppCompatActivity) : DialogNavigatorInterface {

    override fun show(route: DialogRoute) {
        val tag = route.getId()
        val dialog = route.createDialog()
        dialog.show(activity.supportFragmentManager, tag)
    }

    override fun dismiss(route: DialogRoute) {
        val tag = route.getId()
        val dialog = activity.supportFragmentManager.findFragmentByTag(tag) as? DialogFragment
        dialog?.dismiss()
    }
}