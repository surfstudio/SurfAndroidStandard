package ru.surfstudio.android.google_pay_button_sample

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.sample_activity_google_pay_button.*
import ru.surfstudio.android.google_pay_button.GooglePayButton
import ru.surfstudio.android.google_pay_button_sample.view.DefaultGooglePayButton
import ru.surfstudio.android.google_pay_button_sample.view.RoundBlackGooglePayButton
import ru.surfstudio.android.utilktx.util.ViewUtil
import kotlin.random.Random

internal class GooglePayButtonSampleActivityView : AppCompatActivity(R.layout.sample_activity_google_pay_button) {

    private val randomizer = Random(System.currentTimeMillis())

    private val styleToTitleMap = mapOf(
            GooglePayButton.Style.BLACK_LOGO to "black button with \"GPay\" logotype",
            GooglePayButton.Style.BLACK_TEXT to "black button with \"Buy with GPay\" text",
            GooglePayButton.Style.BLACK_FLAT_LOGO to "black flat button with \"GPay\" logotype",
            GooglePayButton.Style.BLACK_FLAT_TEXT to "black flat button with \"Buy with GPay\" text",
            GooglePayButton.Style.WHITE_LOGO to "white button with \"GPay\" logotype",
            GooglePayButton.Style.WHITE_TEXT to "white button with \"Buy with GPay\" text",
            GooglePayButton.Style.WHITE_FLAT_LOGO to "white flat button with \"GPay\" logotype",
            GooglePayButton.Style.WHITE_FLAT_TEXT to "white flat button with \"Buy with GPay\" text"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate_button.setOnClickListener { inflateRandomGooglePayButtons() }
        inflateRandomGooglePayButtons()
    }

    private fun inflateRandomGooglePayButtons() {
        inflated_buttons.removeAllViews()
        for (iteration in 1..INFLATED_BUTTONS_COUNT) {
            inflateGooglePayButton()
        }
    }

    private fun inflateGooglePayButton() {
        val styleOrdinal = randomizer.nextInt(from = 0, until = GooglePayButton.Style.WHITE_FLAT_TEXT.ordinal)
        val style = GooglePayButton.Style.getByOrdinal(styleOrdinal)
        val isEnabled = randomizer.nextBoolean()
        val isBlack = style == GooglePayButton.Style.BLACK_LOGO || style == GooglePayButton.Style.BLACK_TEXT
        val isRoundBlack = isBlack && randomizer.nextBoolean()

        val titleText = StringBuilder().apply {
            append(if (isRoundBlack) "Custom round " else "Default ")
            append(if (isEnabled) "enabled " else "disabled ")
            append(styleToTitleMap[style].orEmpty())
        }
        val titleView = TextView(applicationContext).apply {
            text = titleText
            gravity = Gravity.CENTER_HORIZONTAL
        }
        val titleLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dpToPx(32)
        }

        val buttonView = when {
            isRoundBlack -> RoundBlackGooglePayButton(applicationContext)
            else -> DefaultGooglePayButton(applicationContext)
        }.apply {
            this.isEnabled = isEnabled
            setStyle(style)
        }
        val buttonLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            leftMargin = dpToPx(32)
            rightMargin = dpToPx(32)
            topMargin = dpToPx(8)
        }

        inflated_buttons.addView(titleView, titleLayoutParams)
        inflated_buttons.addView(buttonView, buttonLayoutParams)
    }

    private fun dpToPx(dp: Int): Int {
        return ViewUtil.convertDpToPx(applicationContext, dp.toFloat())
    }

    private companion object {
        const val INFLATED_BUTTONS_COUNT = 20
    }
}
