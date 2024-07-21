package io.nerdythings.modifier.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.nerdythings.modifier.ui.OkHttpProfilerSettings

open class OkHttpProfilerSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: OkHttpProfilerSettingsViewModel =
            ViewModelProvider.create(
                owner = this,
                factory = OkHttpViewModelProvider(application),
            )[OkHttpProfilerSettingsViewModel::class]

        setContent {
            val allSavedPathModifications by viewModel
                .allSavedPathModifications
                .collectAsStateWithLifecycle()

            OkHttpProfilerSettings(
                allSavedPathModifications = allSavedPathModifications,
                onSaveResponse = viewModel::saveCustomResponse,
                onUpdateResponse = viewModel::updateCustomResponse,
                onRemoveCustomResponse = viewModel::removeCustomResponse,
                onClose = { this.finish() },
            )
        }
    }

    companion object {
        /**
         * Returns the intent that the users of this library need to use for starting the
         * Plugin settings activity.
         *
         * @param context Android context
         */
        fun getIntent(context: Context) =
            Intent(context, OkHttpProfilerSettingsActivity::class.java)
    }
}
