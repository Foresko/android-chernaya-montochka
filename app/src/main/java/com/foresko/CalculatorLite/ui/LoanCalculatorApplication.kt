package com.foresko.CalculatorLite.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.foresko.CalculatorLite.ui.destinations.NavGraphs
import com.foresko.CalculatorLite.ui.navGraphs.RootNavigator
import com.foresko.CalculatorLite.utils.LocalModalBottomSheetState
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency

@Composable
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class,
)
fun LoanCalculatorApplication() {
    val navController = rememberNavController()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        )
    )

    staticCompositionLocalOf<ModalBottomSheetState> { error("") }

    val bottomSheetNavigator = rememberBottomSheetNavigator(sheetState = sheetState)
    navController.navigatorProvider += bottomSheetNavigator

    CompositionLocalProvider(LocalModalBottomSheetState provides sheetState) {
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            DestinationsNavHost(
                modifier = Modifier.fillMaxSize(),
                navGraph = NavGraphs.root,
                navController = navController,
                engine = rememberAnimatedNavHostEngine(),
                dependenciesContainerBuilder = {
                    dependency(RootNavigator(destinationsNavigator))
                }
            )
        }
    }
}