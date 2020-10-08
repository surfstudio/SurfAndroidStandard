package ru.surfstudio.android.navigation.sample_standard.screen.dialogs

import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

class DialogsRoute : ActivityRoute() {
    override fun getScreenClass(): Class<out AppCompatActivity> = DialogsActivityView::class.java
}