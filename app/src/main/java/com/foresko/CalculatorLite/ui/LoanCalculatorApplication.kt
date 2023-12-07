package com.foresko.CalculatorLite.ui


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.foresko.CalculatorLite.ui.destinations.NavGraphs
import com.foresko.CalculatorLite.ui.destinations.destinations.MainScreenDestination
import com.foresko.CalculatorLite.ui.destinations.destinations.SelectCountryDestination
import com.foresko.CalculatorLite.ui.navGraphs.RootNavigator
import com.foresko.CalculatorLite.utils.LocalModalBottomSheetState
import com.google.accompanist.navigation.material.ModalBottomSheetLayout


@Composable
@OptIn(
    ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class,
    ExperimentalMaterialApi::class
)
fun LoanCalculatorApplication(
    viewModel: LoanCalculatorApplicationViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    val isFirstStart by viewModel.isFirstStart.collectAsState()

    val startDestination = when (isFirstStart) {
        true -> SelectCountryDestination
        false -> MainScreenDestination
        else -> null
    }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = SwipeableDefaults.AnimationSpec,
        confirmValueChange = { true }
    )

    val bottomSheetNavigator = rememberBottomSheetNavigator(sheetState = sheetState)
    navController.navigatorProvider += bottomSheetNavigator

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    CompositionLocalProvider(LocalModalBottomSheetState provides sheetState) {
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            if (startDestination != null) {
                DestinationsNavHost(
                    modifier = Modifier.fillMaxSize(),
                    navGraph = NavGraphs.root,
                    startRoute = startDestination,
                    engine = rememberAnimatedNavHostEngine(),
                    navController = navController,
                    dependenciesContainerBuilder = {
                        dependency(RootNavigator(destinationsNavigator))
                    }
                )
            }
        }
    }
}