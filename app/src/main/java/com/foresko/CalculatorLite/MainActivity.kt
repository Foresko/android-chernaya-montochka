package com.foresko.CalculatorLite

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.foresko.CalculatorLite.utils.LocalActivity
import dagger.hilt.android.AndroidEntryPoint
import com.foresko.CalculatorLite.ui.LoanCalculatorApplication

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContent {
            CompositionLocalProvider(LocalActivity provides this@MainActivity) {
                LoanCalculatorApplication()
            }
        }
    }
}