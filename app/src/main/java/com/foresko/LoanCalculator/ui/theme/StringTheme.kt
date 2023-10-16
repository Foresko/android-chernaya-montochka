package com.foresko.LoanCalculator.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class LoanStyles(
    val body: TextStyle,
    val decoratingText: TextStyle,
    val mainText: TextStyle,
    val dateDecoratingText: TextStyle,
    val buttonText: TextStyle,
    val numberText: TextStyle
) {
    constructor() : this(
        body = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
            lineHeight = 24.sp
        ),
        decoratingText = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight(500),
            lineHeight = 29.sp
        ),
        dateDecoratingText = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight(500),
            lineHeight = 24.sp
        ),
        mainText = TextStyle(
            fontSize = 17.sp,
            fontWeight = FontWeight(400),
            lineHeight = 20.sp
        ),
        buttonText = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight(400),
            lineHeight = 22.sp
        ),
        numberText = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(600),
            lineHeight = 24.sp
        )
    )
}

val LocalLoanStyles = staticCompositionLocalOf { LoanStyles() }