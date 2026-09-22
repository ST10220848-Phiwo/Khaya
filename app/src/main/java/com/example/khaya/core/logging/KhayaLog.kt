package com.example.khaya.core.logging

import android.util.Log

/**
 * A wrapper class for logging using android.util.Log
 *
 * Routing everything here so that Logcat can be filtered by layer:
 * Khaya/Lifecycle --> Activity / Application / ViewModel lifecycle callbacks
 * Khaya/State --> state transitions and UI updates
 * Khaya/Net --> API calls and responses
 * Filtering Logcat with "tag:Khaya/" shows the app in a single view instead of having multiple tabs.
 *
 */
object KhayaLog {
    private const val PREFIX = "Khaya/"

    fun lifecycle(component: String, event: String) {
        Log.i("${PREFIX}Lifecycle", "[$component]: $event")
    }

    /** Both sides are logged capturing the "before" and "after" is visible */
    fun state(owner: String, from: Any?, to: Any?) {
        d("${PREFIX}State", "[$owner] ${from.describe()} -> ${to.describe()}")
    }

    /** Prints class names for states, full data objects could leak a childs
     * location, coordinates go to Logcat
     */
    private fun Any?.describe(): String = this?.toString() ?: "null"
    fun d(tag: String, msg: String) = Log.d(PREFIX + tag, msg)
    fun i(tag: String, msg: String) = Log.i(PREFIX + tag, msg)
    fun w(tag: String, msg: String, tr: Throwable? = null) = Log.w(PREFIX + tag, msg, tr)
    fun e(tag: String, msg: String, tr: Throwable? = null) = Log.e(PREFIX + tag, msg, tr)
}
