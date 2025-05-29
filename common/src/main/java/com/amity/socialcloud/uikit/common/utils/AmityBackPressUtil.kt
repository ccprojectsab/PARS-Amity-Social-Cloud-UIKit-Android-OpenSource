package com.amity.socialcloud.uikit.common.utils

import android.app.ActivityManager
import android.content.Context
import androidx.fragment.app.FragmentManager

object AmityBackPressUtil {
     fun isLastActivity(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.appTasks
        if (tasks.isNotEmpty()) {
            val taskInfo = tasks[0].taskInfo
            if (taskInfo.numActivities == 1) {
                return true
            }
        }
        return false
    }

     fun isBackStackEmpty(parentFragmentManager: FragmentManager): Boolean {
        return parentFragmentManager.backStackEntryCount == 0
    }

    fun isLastActivityAndBackStackEmpty(
        context: Context,
        parentFragmentManager: FragmentManager
    ): Boolean {
        return isLastActivity(context) && isBackStackEmpty(parentFragmentManager)
    }
}