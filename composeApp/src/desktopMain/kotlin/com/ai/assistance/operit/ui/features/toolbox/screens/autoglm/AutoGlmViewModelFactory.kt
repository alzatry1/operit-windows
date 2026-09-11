package com.ai.assistance.operit.ui.features.toolbox.screens.autoglm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

class AutoGlmViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
        if (modelClass.java.isAssignableFrom(AutoGlmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AutoGlmViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
