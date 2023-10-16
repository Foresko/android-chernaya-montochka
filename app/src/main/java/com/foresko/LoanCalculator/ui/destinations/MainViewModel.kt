package com.foresko.LoanCalculator.ui.destinations

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresko.LoanCalculator.core.rest.Loan
import com.foresko.LoanCalculator.core.rest.MicroloansRequest
import com.foresko.LoanCalculator.network.NetworkStatusTracker
import com.foresko.LoanCalculator.ui.enumClass.FilterType
import com.foresko.LoanCalculator.ui.enumClass.TimePeriod
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkStatusTracker: NetworkStatusTracker,
    private val allOffersRequest: MicroloansRequest,
): ViewModel() {
    private var _offers = MutableStateFlow<List<Loan>?>(null)

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

    fun daysDifference(startDate: String, endDate: String): Int {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        try {
            val start = dateFormat.parse(startDate)
            val end = dateFormat.parse(endDate)

            if (start != null && end != null) {
                val diff = end.time - start.time
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
            }
        } catch (e: Exception) {
        }
        return 0
    }

    private val _selectedPeriod = MutableLiveData(TimePeriod.DAY)
    val selectedPeriod: LiveData<TimePeriod> get() = _selectedPeriod

    fun setSelectedPeriod(period: TimePeriod) {
        _selectedPeriod.value = period
    }


    var activeFilterType by mutableStateOf(FilterType.Rating)
        private set

    fun onActiveFilterChange(activeFilter: FilterType) {
        this.activeFilterType = activeFilter
        _offers.value = when (activeFilter) {
            FilterType.Rating -> _offers.value?.sortedByDescending { it.rating }
            FilterType.Rate -> _offers.value?.sortedBy { it.rateFrom }
            FilterType.Sum -> _offers.value?.sortedByDescending { it.sumTo.toFloat() }
            FilterType.Term -> _offers.value?.sortedByDescending { it.termTo.toFloat() }
        }
    }

    var sumKeyboardState by mutableStateOf<Pair<KeyboardType?, Boolean>>(null to false)
        private set

    init {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest {
                getMortgages()
            }
        }
    }

    private suspend fun getMortgages() {
        try {
            _offers.value = allOffersRequest.infoGet()
        } catch (e: Exception) {
            Log.d("mortgages", "error = $e")
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

}