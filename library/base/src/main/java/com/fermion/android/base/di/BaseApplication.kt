package com.fermion.android.base.di

import android.app.Application
import com.fermion.android.base.BuildConfig
import com.fermion.android.base.helper.CrashReportingTree
import io.paperdb.Paper
import timber.log.Timber

/**
 * Created by Bhavesh Auodichya.
 *
 *Application class used for injecting hilt and other default initialization
 *
 * Extend this to your application class and mention it in manifest
 *
 *@since 1.0.0
 */

abstract class BaseApplication : Application() {
    init {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        else Timber.plant(CrashReportingTree())
    }

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)

    }
}