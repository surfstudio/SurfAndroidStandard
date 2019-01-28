package ru.surfstudio.android.security.ui

import android.app.Activity
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText

/**
 * @param menuIds id элементов контекстного меню у EditText. Прим. android.R.id.copy, android.R.id.cut.
 */
fun EditText.deleteContextMenuItems(vararg menuIds: Int) {
    this.customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menuIds.forEach { menuId ->
                val menuItem = menu?.findItem(menuId)
                menuItem?.let {
                    menu.removeItem(menuId)
                }
            }
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean = false

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

        override fun onDestroyActionMode(mode: ActionMode?) {
            //empty
        }
    }
}

/**
 * Добавляет флаг [WindowManager.LayoutParams.FLAG_SECURE] для Activity.
 */
fun Activity.enableSecureMode() {
    window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
    )
}

/**
 * Удаляет флаг [WindowManager.LayoutParams.FLAG_SECURE] у Activity.
 */
fun Activity.disableSecureMode() {
    window.clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
    )
}