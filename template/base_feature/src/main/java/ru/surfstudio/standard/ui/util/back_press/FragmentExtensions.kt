package ru.surfstudio.standard.ui.util.back_press

import androidx.fragment.app.Fragment

fun Fragment.addDefaultOnBackPressedCallback(onBackPressed: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(this, DefaultBackPressedCallback(onBackPressed))
}
