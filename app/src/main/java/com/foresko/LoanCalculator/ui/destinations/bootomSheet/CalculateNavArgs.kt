package com.foresko.LoanCalculator.ui.destinations.bootomSheet

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
data class CalculateNavArgs(
    val differenceInDays: Int,
    val sumAmount: String,
    val percentRate: String,
) : Parcelable