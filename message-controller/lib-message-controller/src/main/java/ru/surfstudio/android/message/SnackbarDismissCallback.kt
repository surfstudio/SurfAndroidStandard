package ru.surfstudio.android.message

import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar

internal class SnackbarDismissCallback(
        private val dismissListener: (DismissReason) -> Unit
) : Snackbar.Callback() {

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        dismissListener(mapDismissReason(event))
    }

    private fun mapDismissReason(@DismissEvent event: Int): DismissReason {
        return when (event) {
            BaseCallback.DISMISS_EVENT_ACTION -> DismissReason.ACTION
            BaseCallback.DISMISS_EVENT_CONSECUTIVE -> DismissReason.CONSECUTIVE
            BaseCallback.DISMISS_EVENT_MANUAL -> DismissReason.MANUAL
            BaseCallback.DISMISS_EVENT_SWIPE -> DismissReason.SWIPE
            BaseCallback.DISMISS_EVENT_TIMEOUT -> DismissReason.TIMEOUT
            else -> error("Unexpected dismiss event($event)")
        }
    }
}
