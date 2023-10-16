package com.foresko.LoanCalculator.ui.enumClass

import com.foresko.LoanCalculator.R

enum class FilterType(val labelId: Int) {
    Rating(labelId = R.string.rating_filter),
    Rate(labelId = R.string.rate_filter),
    Sum(labelId = R.string.sum_filter),
    Term(labelId = R.string.term_filter)
}