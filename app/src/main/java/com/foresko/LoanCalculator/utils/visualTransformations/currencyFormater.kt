package com.foresko.LoanCalculator.utils.visualTransformations

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

val currencySymbolMap: Map<String, String> = mapOf(
    "RUB" to "₽",
    "KZT" to "₸",
    "PLN" to "zł",
    "RON" to "lei",
    "COP" to "$",
    "MXN" to "$",
    "VND" to "₫",
    "PHP" to "₱",
    "USD" to "$"
)

fun currencyFormatter(currencyCode: String): NumberFormat {
    val locale = Locale.forLanguageTag(currencyCode)

    val currency = try {
        Currency.getInstance(currencyCode)
    } catch (ex: Exception) {
        Currency.getInstance(Locale.getDefault())
    }

    val decimalFormatSymbols = DecimalFormatSymbols(locale).apply {
        currencySymbol = currencySymbolMap[currencyCode.uppercase()] ?: currency.symbol
    }

    val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
        this.maximumFractionDigits = 0
    } as DecimalFormat
    numberFormat.decimalFormatSymbols = decimalFormatSymbols

    return numberFormat
}