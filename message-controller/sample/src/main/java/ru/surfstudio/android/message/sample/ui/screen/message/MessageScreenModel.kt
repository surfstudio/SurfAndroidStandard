package ru.surfstudio.android.message.sample.ui.screen.message

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.message.SnackParams
import ru.surfstudio.android.message.ToastParams

class MessageScreenModel : ScreenModel() {

    var snackParams: SnackParams? = SnackParams()
    var toastParams: ToastParams? = ToastParams()
}