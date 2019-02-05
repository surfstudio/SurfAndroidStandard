/*
  Copyright (c) 2018-present, SurfStudio LLC, Margarita Volodina, Oleg Zhilo.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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