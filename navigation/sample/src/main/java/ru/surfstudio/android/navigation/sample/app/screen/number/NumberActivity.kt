package ru.surfstudio.android.navigation.sample.app.screen.number

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_number.*
import ru.surfstudio.android.navigation.sample.R

/**
 * Простая активити, отображающая в TextView номер, переданный в роут
 */
class NumberActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number)

        val route = NumberRoute(intent)
        activity_number_tv.text = route.number.toString()
    }
}