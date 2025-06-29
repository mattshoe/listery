package org.mattshoe.shoebox.listery

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.hilt.android.HiltAndroidApp
import org.mattshoe.shoebox.listery.logging.logi
import org.mattshoe.shoebox.listery.util.ActivityProvider

@HiltAndroidApp
class ListeryApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Register activity lifecycle callbacks to monitor all activities
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityProvider.currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                logi("Activity Started: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivityResumed(activity: Activity) {
                logi("Activity Resumed: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivityPaused(activity: Activity) {
                logi("Activity Paused: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivityStopped(activity: Activity) {
                logi("Activity Stopped: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                logi("Activity SaveInstanceState: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityProvider.currentActivity = activity
            }
        })
    }

}