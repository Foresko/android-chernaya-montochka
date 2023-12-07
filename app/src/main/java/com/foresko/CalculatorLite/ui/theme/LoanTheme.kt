package com.foresko.CalculatorLite.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object LoanTheme {

    val colors: LoanColors
        @Composable
        @ReadOnlyComposable
        get() = LocalLoanColors.current

    val textStyles: LoanStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalLoanStyles.current
}

@Composable
fun LoanTheme(content: @Composable () -> Unit) {
    val rippleIndication = rememberRipple()

    CompositionLocalProvider(
        LocalIndication provides rippleIndication,
    ) {
        val systemUiController = rememberSystemUiController()

        LaunchedEffect(systemUiController) {
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = true,
                transformColorForLightContent = { Color(0x66000000) }
            )

            systemUiController.isNavigationBarContrastEnforced = true

            if (systemUiController.isNavigationBarContrastEnforced) {
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = true,
                    transformColorForLightContent = { Color(0x66000000) }
                )
            } else {
                systemUiController.setNavigationBarColor(
                    color = Color(0xE6FFFFFF),
                    darkIcons = false,
                    transformColorForLightContent = { Color(0x66000000) }
                )
            }
        }

        content()
    }
}