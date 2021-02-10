package ru.surfstudio.android.navigation.sample.app.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.navigation.sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        if (savedInstanceState == null) { //Adding fragment only on first create
            // TODO
        }
    }
}