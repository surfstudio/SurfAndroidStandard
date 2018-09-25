package ru.surfstudio.standard.app_injector

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.template.app_injector.BuildConfig
import ru.surfstudio.android.template.app_injector.R
import ru.surfstudio.android.utilktx.util.SdkUtils
import ru.surfstudio.standard.app_injector.ui.screen.configurator.storage.ScreenConfiguratorStorage
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider
import ru.surfstudio.standard.f_debug.debug.DebugActivityRoute
import ru.surfstudio.standard.f_debug.debug.DebugActivityView

class App : CoreApp() {

    companion object {
        private val DEBUG_NOTIFICATION_ID = DebugActivityView::class.hashCode()
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.initInjector(this)

        initFabric()
        initComponentProvider()
        initDebugNotification()
    }

    private fun initComponentProvider() {
        ComponentProvider.createActivityScreenConfigurator = { intent, kclass ->
            ScreenConfiguratorStorage.activityScreenConfiguratorMap[kclass]?.invoke(intent)!!
        }

        ComponentProvider.createActivityConfigurator = { intent, kclass ->
            ScreenConfiguratorStorage.activityConfiguratorMap[kclass]?.invoke(intent)!!
        }

        ComponentProvider.createFragmentScreenConfigurator = { bundle, kclass ->
            ScreenConfiguratorStorage.fragmentScreenConfiguratorMap[kclass]?.invoke(bundle)!!
        }

        ComponentProvider.createDialogScreenConfigurator = { bundle, kclass ->
            ScreenConfiguratorStorage.dialogScreenConfiguratorMap[kclass]?.invoke(bundle)!!
        }

        ComponentProvider.createWidgetScreenConfigurator = { kclass ->
            ScreenConfiguratorStorage.widgetScreenConfiguratorMap[kclass]?.invoke()!!
        }
    }

    private fun initFabric() {
        Fabric.with(this, *getFabricKits())
    }

    private fun getFabricKits() = arrayOf(Crashlytics.Builder()
            .core(CrashlyticsCore.Builder()
                    .disabled(BuildConfig.DEBUG)
                    .build())
            .build())

    @SuppressLint("NewApi")
    private fun initDebugNotification() {
        if (canShowDebugScreen()) {
            val channelId = getString(R.string.notification_channel_id)
            val notificationTitle = getString(R.string.debug_screen_title)
            val notificationBody = getString(R.string.debug_screen_body)

            val pendingIntent = PendingIntent.getActivity(
                    this,
                    DEBUG_NOTIFICATION_ID,
                    DebugActivityRoute().prepareIntent(this),
                    PendingIntent.FLAG_ONE_SHOT)

            val notificationBuilder =  NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationBody)
                    .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (SdkUtils.isAtLeastOreo) {
                notificationManager.createNotificationChannel(
                        NotificationChannel(
                                channelId,
                                notificationTitle,
                                NotificationManager.IMPORTANCE_HIGH).apply {
                            description = notificationBody
                        }
                )
            }

            notificationManager.notify(DEBUG_NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun canShowDebugScreen(): Boolean {
        return BuildConfig.BUILD_TYPE == "debug" || BuildConfig.BUILD_TYPE == "qa"
    }
}