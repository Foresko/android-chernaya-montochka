package com.foresko.CalculatorLite.ui.destinations.bootomSheet

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalculateNavArgs(
    val differenceInDays: Int,
    val sumAmount: String,
    val percentRate: String,
) : Parcelable