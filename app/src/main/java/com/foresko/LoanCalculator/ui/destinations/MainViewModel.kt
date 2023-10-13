package com.foresko.LoanCalculator.ui.destinations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.foresko.LoanCalculator.core.rest.MicroLoan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

): ViewModel() {
    private var _offers = MutableStateFlow<List<MicroLoan>?>(null)

    val offers = _offers.asStateFlow()

    var sumAmount by mutableStateOf("")
        private set

    var percentRate by mutableStateOf("")
        private set

    var startDate by mutableStateOf("")
        private set

    var finalDate by mutableStateOf("")
        private set

    fun changeSumAmount(newSumAmount: String) {
        sumAmount = newSumAmount.trim()
    }

    fun changePercentRate(newPercentRate: String) {
        percentRate = newPercentRate.trim()
    }

    fun changeStartDate(newStartDate: String) {
        startDate = newStartDate.trim()
    }

    fun changeFinalDate(newFinalDate: String) {
        finalDate = newFinalDate.trim()
    }
}