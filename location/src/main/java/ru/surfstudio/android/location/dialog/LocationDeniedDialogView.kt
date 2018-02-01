package ru.surfstudio.android.location.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import ru.surfstudio.android.R
import ru.surfstudio.android.core.ui.base.delegate.activity.result.SupportOnActivityResultRoute.EXTRA_RESULT
import ru.surfstudio.android.core.ui.base.navigation.Route


class LocationDeniedDialogView : AppCompatActivity() {
    private val appSettingsRequestCode: Int = 2244

    private var titleTv: TextView? = null
    private var messageTv: TextView? = null
    private var cancelBtn: Button? = null
    private var goToSettingsBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_location_denied)

        val data = intent.getSerializableExtra(Route.EXTRA_FIRST) as LocationDeniedDialogData

        title = data.title

        initViews()
        render(data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == appSettingsRequestCode) {
            finishWithResult(resultCode == RESULT_OK)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initViews() {
        titleTv = findViewById(R.id.dialog_location_denied_title_tv)
        messageTv = findViewById(R.id.dialog_location_denied_message_tv)

        cancelBtn = findViewById(R.id.dialog_location_denied_cancel_btn)
        cancelBtn?.setOnClickListener { finishWithResult(false) }

        goToSettingsBtn = findViewById(R.id.dialog_location_denied_go_to_settings_btn)
        goToSettingsBtn?.setOnClickListener { openAppSettings() }
    }

    private fun render(data: LocationDeniedDialogData) {
        titleTv?.text = data.title
        titleTv?.visibility = if (data.title != null) View.VISIBLE else View.GONE

        messageTv?.text = data.message
        cancelBtn?.text = data.cancelBtn

        goToSettingsBtn?.text = data.goToSettingsBtn
        goToSettingsBtn?.visibility = if (data.goToSettingsBtn != null) View.VISIBLE else View.GONE
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + packageName))
        startActivityForResult(intent, appSettingsRequestCode)
    }

    private fun finishWithResult(result: Boolean) {
        val data = Intent()
        data.putExtra(EXTRA_RESULT, result)
        setResult(if (result) RESULT_OK else RESULT_CANCELED, data)
        finish()
    }
}
