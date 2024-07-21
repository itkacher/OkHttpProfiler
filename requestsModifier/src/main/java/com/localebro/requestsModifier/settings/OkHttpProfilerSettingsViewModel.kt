package com.localebro.requestsModifier.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.localebro.requestsModifier.dataModifier.CustomResponse
import com.localebro.requestsModifier.dataModifier.DataModifier
import kotlinx.coroutines.launch

internal class OkHttpProfilerSettingsViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private var dataModifier: DataModifier = DataModifier(application)

    internal val allSavedPathModifications = dataModifier.customResponses

    init {
        viewModelScope.launch {
            dataModifier.fetchAllCustomResponses()
        }
    }

    internal fun saveCustomResponse(path: String, response: String, responseCode: Int) {
        viewModelScope.launch {
            dataModifier.createCustomResponse(path, response, responseCode)
        }
    }

    internal fun updateCustomResponse(customResponse: CustomResponse) {
        viewModelScope.launch {
            dataModifier.updateCustomResponse(customResponse)
        }
    }

    internal fun removeCustomResponse(uuid: String) {
        viewModelScope.launch {
            dataModifier.removeCustomResponse(uuid)
        }
    }
}

class OkHttpViewModelProvider(
    private val application: Application,
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OkHttpProfilerSettingsViewModel(application) as T
    }
}