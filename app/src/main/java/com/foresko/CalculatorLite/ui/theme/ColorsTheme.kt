package com.foresko.CalculatorLite.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class LoanColors(
    white: Color,
    black: Color,
    textFieldColor: Color,
    borderTextField: Color,
    gray: Color,
    decoratingText: Color,
    noActiveButton: Color,
    blueText: Color,
    surface: Color,
    grayButton: Color

) {
    var white: Color by mutableStateOf(white)
        private set

    var black: Color by mutableStateOf(black)
        private set

    var textFieldColor: Color by mutableStateOf(textFieldColor)
        private set

    var borderTextField: Color by mutableStateOf(borderTextField)
        private set

    var gray: Color by mutableStateOf(gray)
        private set

    var decoratingText: Color by mutableStateOf(decoratingText)
        private set

    var noActiveButton: Color by mutableStateOf(noActiveButton)
        private set

    var blueText: Color by mutableStateOf(blueText)
        private set

    var surface: Color by mutableStateOf(surface)
        private set

    var grayButton: Color by mutableStateOf(grayButton)
        private set


    override fun toString(): String {
        return """LoanColors(
            white=$white,
            black=$black,
            textFieldColor=$textFieldColor,
            borderTextField=$borderTextField,
            gray=$gray,
            decoratingText=$decoratingText.
            noActiveButton=$noActiveButton
            blueText=$blueText
            surface=$surface
            grayButton=$grayButton
        )"""
    }
}

fun darkColors(): LoanColors = LoanColors(
    white = Color(0xFFFFFFFF),
    black = Color(0xFF000000),
    textFieldColor = Color(0xFFF7F7F7),
    borderTextField = Color(0xFF60D06B),
    gray = Color(0xFFBABEC9),
    decoratingText = Color(0xFF343D4E),
    noActiveButton = Color(0xFF5C725E),
    blueText = Color(0xFF1A55AF),
    surface = Color(0xFFF3F5FD),
    grayButton = Color(0xFFBABEC9)
)

val LocalLoanColors = staticCompositionLocalOf { darkColors() }