package com.mindgate.mindgateapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mindgate.mindgateapp.data.dao.Professional
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class ProfessionalViewModel @Inject constructor() : ViewModel() {
    val selectedProfessional = mutableStateOf<Professional?>(null)

    fun setSelectedProfessional(professional: Professional) {
        selectedProfessional.value = professional
    }

}