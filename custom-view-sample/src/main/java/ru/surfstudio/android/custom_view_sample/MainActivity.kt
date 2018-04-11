package ru.surfstudio.android.custom_view_sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.custom_view_sample.R.id.placeholder_view

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        placeholder_view.render(LoadState.MAIN_LOADING)
        Log.d("1111", "1111 oncreate")
    }
}