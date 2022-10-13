package com.spiraclesoftware.androidsample

import android.app.Application
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Behavior
import com.pandulapeter.beagle.log.BeagleLogger
import com.pandulapeter.beagle.logOkHttp.BeagleOkHttpLogger
import com.pandulapeter.beagle.modules.*
import com.spiraclesoftware.androidsample.common.commonModule
import com.spiraclesoftware.androidsample.data_local.localModule
import com.spiraclesoftware.androidsample.data_remote.remoteModule
import com.spiraclesoftware.androidsample.domain.domainModules
import com.spiraclesoftware.androidsample.feature.featureModules
import com.spiraclesoftware.androidsample.framework.utils.ThemeManager
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKoin()
        initBeagle()
        applyTheming()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SampleApplication)
            modules(
                listOf(
                    appModule,
                    commonModule,
                    localModule,
                    remoteModule
                ) + featureModules + domainModules
            )
        }
    }

    private fun initBeagle() {
        Beagle.initialize(
            application = this,
            behavior = Behavior(
                logBehavior = Behavior.LogBehavior(
                    loggers = listOf(BeagleLogger),
                ),
                networkLogBehavior = Behavior.NetworkLogBehavior(
                    networkLoggers = listOf(BeagleOkHttpLogger),
                ),
            )
        )

        Beagle.set(
            HeaderModule(
                title = getString(R.string.app_name),
                subtitle = "${BuildConfig.BUILD_TYPE} v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            ),
            PaddingModule(),
            TextModule("Logs", TextModule.Type.SECTION_HEADER),
            NetworkLogListModule(),
            LogListModule(),
            LifecycleLogListModule(),
            DividerModule(),
            TextModule("Other", TextModule.Type.SECTION_HEADER),
            DeviceInfoModule(),
            ScreenCaptureToolboxModule(),
            KeylineOverlaySwitchModule(),
            AnimationDurationSwitchModule(),
        )
    }

    private fun initTimber() {
        class BeagleTree : DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                Beagle.log("\n[$tag] $message", "Timber", t?.stackTraceToString())
            }
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Timber.plant(BeagleTree())
        }
    }

    private fun applyTheming() {
        get<ThemeManager>().applyCurrentTheme()
    }

}