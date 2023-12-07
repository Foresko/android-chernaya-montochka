package com.foresko.CalculatorLite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresko.CalculatorLite.dataStore.FirstStartDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoanCalculatorApplicationViewModel @Inject constructor(
    private val firstStartDataStore: FirstStartDataStore
) : ViewModel() {

    val isFirstStart = firstStartDataStore.data.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    fun completeFirstStartProcess() {
        viewModelScope.launch {
            firstStartDataStore.updateData { false }
        }
    }
}
