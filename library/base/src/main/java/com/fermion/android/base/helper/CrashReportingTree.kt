package com.fermion.android.base.helper

import android.util.Log
import timber.log.Timber


/**
 * Created by Bhavesh Auodichya.
 *
 * Custom timber log class
 *
 * **Info** Here you can log you custom error to local or remote server
 *@since 1.0.0
 */
class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) return

        //FakeCrashLibrary.log(priority, tag, message)

        if (t != null) {
            if (priority == Log.ERROR) {
                //FakeCrashLibrary.logError(t)
            } else if (priority == Log.WARN) {
                //FakeCrashLibrary.logWarning(t)
            }
        }
    }
}