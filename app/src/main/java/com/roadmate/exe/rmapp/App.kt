package com.roadmate.exe.rmapp

import android.app.Application
import com.roadmate.exe.constants.SharedPreferenceConstants.Companion.PREF_NAME
import com.roadmate.exe.log.AppLogger
import com.roadmate.exe.preference.Preference
import org.jetbrains.anko.getStackTraceString


/**
 * App - Android application class
 */
class App : Application() {

    companion object {
        lateinit var mApp: App
    }

    /**
     * onCreate
     *
     * Initialize Room database, initialize Preference and set un caught exception
     */
    override fun onCreate() {
        mApp = this

        super.onCreate()

        Preference.init(this, PREF_NAME)

        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            AppLogger.error(e.getStackTraceString())
        }

    }

    override fun onTerminate() {
        super.onTerminate()
    }
}