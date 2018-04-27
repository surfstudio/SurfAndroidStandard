package ru.surfstudio.android.custom_view_sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.custom_view_sample.placeholder.PlaceHolderView

class MainActivity : AppCompatActivity() {

    lateinit var placeholderView: PlaceHolderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        placeholderView = findViewById(R.id.placeholder_view)
        placeholderView.render(LoadState.MAIN_LOADING)
        var x = 0
        change_state_btn.setOnClickListener {
            if (x == 0) {
                placeholderView.render(LoadState.ERROR)
                x++

            } else if (x == 1) {
                placeholderView.render(LoadState.MAIN_LOADING)
                x--
            }
        }
    }
}