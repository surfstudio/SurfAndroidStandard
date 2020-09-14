package ru.surfstudio.android.google_pay_button_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.sample_activity_google_pay_button.*

class GooglePayButtonSampleActivityView : AppCompatActivity(R.layout.sample_activity_google_pay_button) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listOf(
                black_flat_gpay_disabled,
                black_flat_gpay_titled_disabled,
                black_gpay_disabled,
                black_gpay_titled_disabled,
                white_flat_gpay_disabled,
                white_flat_gpay_titled_disabled,
                white_gpay_disabled,
                white_gpay_titled_disabled
        ).forEach { it.isEnabled = false }
    }
}
