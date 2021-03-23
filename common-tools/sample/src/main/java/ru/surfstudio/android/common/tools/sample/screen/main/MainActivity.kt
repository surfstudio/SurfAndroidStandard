package ru.surfstudio.android.common.tools.sample.screen.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.common.tools.sample.R
import ru.surfstudio.android.common.tools.sample.screen.status_bar_switcher.StatusBarSwitcherActivityView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListener(open_status_bar_switcher_btn, StatusBarSwitcherActivityView::class.java)
    }

    private fun setOnClickListener(btn: Button, activityClass: Class<*>) {
        btn.setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
