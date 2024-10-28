package com.foresko.CalculatorLite.ui.destinations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
): ViewModel() {
    private val _storeInfo = MutableStateFlow<List<StoreInfo>?>(null)
    val storeInfo: StateFlow<List<StoreInfo>?> = _storeInfo.asStateFlow()

    var activeFilterType by mutableStateOf(FilterType.Rating)
        private set

    private var originalStoreInfo: List<StoreInfo>? = null

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

    private val _selectedPeriod = MutableStateFlow(TimePeriod.DAY)
    val selectedPeriod: StateFlow<TimePeriod> get() = _selectedPeriod

    fun setSelectedPeriod(period: TimePeriod) {
        _selectedPeriod.value = period
    }

    fun onActiveFilterChange(activeFilter: FilterType) {
        activeFilterType = activeFilter

        _storeInfo.value = when (activeFilter) {
            FilterType.Rating -> originalStoreInfo?.sortedByDescending { it.rating }
            FilterType.Term -> originalStoreInfo?.sortedWith(
                compareByDescending<StoreInfo> { it.termTo }
                    .thenByDescending { it.rating }
            )
            FilterType.Rate -> originalStoreInfo?.sortedWith(
                compareBy<StoreInfo> { it.rateFrom }
                    .thenByDescending { it.rating }
            )
            FilterType.Sum -> originalStoreInfo?.sortedWith(
                compareByDescending<StoreInfo> { it.sumTo }
                    .thenByDescending { it.rating }
            )
        }
    }

    init {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collectLatest {
                getMortgages()
            }
        }
    }


    private fun getMortgages() {
        viewModelScope.launch {
            val result = microloansRepository.getStoreInfo()
            if (result.isSuccess) {
                originalStoreInfo = result.getOrNull()
                _storeInfo.value = originalStoreInfo
            } else {
                originalStoreInfo = null
                _storeInfo.value = null
            }
        }
    }
}