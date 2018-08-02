package ru.surfstudio.standard.base

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.template.base.BuildConfig


open class BaseApp : CoreApp() {

    override fun onCreate() {
        super.onCreate()
        initFabric()
    }

    private fun initFabric() {
        Fabric.with(this, *getFabricKits())
    }

    private fun getFabricKits() =
            arrayOf(Crashlytics.Builder()
                    .core(CrashlyticsCore.Builder()
                            .disabled(BuildConfig.DEBUG)
                            .build())
                    .build())
}