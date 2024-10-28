package com.foresko.CalculatorLite

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import com.foresko.CalculatorLite.ui.LoanCalculatorApplication

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider(LocalActivity provides this@MainActivity) {
                LoanCalculatorApplication()
            }
        }
    }
}

val LocalActivity = staticCompositionLocalOf<Activity> {
    noLocalProvidedFor("LocalActivity")
}

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
