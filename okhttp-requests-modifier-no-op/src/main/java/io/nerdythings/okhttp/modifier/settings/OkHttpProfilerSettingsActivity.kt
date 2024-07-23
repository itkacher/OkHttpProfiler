package io.nerdythings.okhttp.modifier.settings

import android.app.Activity
import android.content.Context
import android.content.Intent

open class OkHttpProfilerSettingsActivity : Activity() {
    companion object {
        fun getIntent(context: Context) = Intent()
    }
}