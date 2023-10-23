package com.foresko.CalculatorLite.ui.enumClass

import com.foresko.CalculatorLite.R

enum class FilterType(val labelId: Int) {
    Rating(labelId = R.string.rating_filter),
    Rate(labelId = R.string.rate_filter),
    Sum(labelId = R.string.sum_filter),
    Term(labelId = R.string.term_filter)
}