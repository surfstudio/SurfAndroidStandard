package ru.surfstudio.android.custom_view_sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.sample.common.ui.base.loadstate.LoadState
import ru.surfstudio.android.sample.common.ui.base.loadstate.renderer.DefaultLoadStateRenderer

class MainActivity : AppCompatActivity() {

    private lateinit var loadStateRenderer: DefaultLoadStateRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadStateRenderer = DefaultLoadStateRenderer(placeholder_view)
        loadStateRenderer.render(LoadState.MAIN_LOADING)
        var x = 0
        change_state_btn.setOnClickListener {
            if (x == 0) {
                loadStateRenderer.render(LoadState.ERROR)
                x++

            } else if (x == 1) {
                loadStateRenderer.render(LoadState.MAIN_LOADING)
                x--
            }
        }
        open_tv_screen_btn.setOnClickListener {
            startActivity(Intent(this, TitleSubtitleViewDemoActivity::class.java))
        }
    }
}