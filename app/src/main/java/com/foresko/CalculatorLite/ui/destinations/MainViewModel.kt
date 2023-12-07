package com.foresko.CalculatorLite.ui.destinations

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresko.CalculatorLite.core.rest.ApiService
import com.foresko.CalculatorLite.core.rest.Country
import com.foresko.CalculatorLite.core.rest.MicroloansRepository
import com.foresko.CalculatorLite.core.rest.StoreInfo
import com.foresko.CalculatorLite.network.NetworkStatusTracker
import com.foresko.CalculatorLite.ui.enumClass.FilterType
import com.foresko.CalculatorLite.ui.enumClass.TimePeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val microloansRepository: MicroloansRepository,
    private val microloansRequest: ApiService,
): ViewModel() {
    private val _storeInfo = MutableStateFlow<List<StoreInfo>?>(null)
    val storeInfo: StateFlow<List<StoreInfo>?> = _storeInfo.asStateFlow()

    private val _activeCountryCode = MutableStateFlow<String>("")
    val activeCountryCode: StateFlow<String> = _activeCountryCode.asStateFlow()

    private val _countries = MutableStateFlow<List<Country>?>(null)
    val countries: StateFlow<List<Country>?> = _countries.asStateFlow()

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
        _storeInfo.value = when (activeFilter) {
            FilterType.Rating -> storeInfo.value?.sortedByDescending { it.rating }
            FilterType.Rate -> storeInfo.value?.sortedBy { it.rateFrom }
            FilterType.Sum -> storeInfo.value?.sortedByDescending { it.sumTo.toFloat() }
            FilterType.Term -> storeInfo.value?.sortedByDescending { it.termTo.toFloat() }
        }
    }

    init {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest {
                getMortgages()
            }
        }

        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest { updateActiveCountryCode() }
        }

        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest {
                loadCountries()
            }
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            val response = microloansRequest.infoGet()
            _countries.value = response.countries
        }
    }

    private fun updateActiveCountryCode() {
        viewModelScope.launch {
            _activeCountryCode.value = microloansRepository.getActiveCountryCode()
        }
    }

    private fun getMortgages() {
        try {
            viewModelScope.launch { _storeInfo.value = microloansRepository.getStoreInfo() }
        } catch (e: Exception) {
            Log.d("mortgages", "error = $e")
        }
    }
}