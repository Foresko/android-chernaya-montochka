package com.foresko.LoanCalculator.ui.destinations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.LoanCalculator.core.rest.MicroLoan
import com.foresko.LoanCalculator.navGraphs.RootNavGraph
import com.foresko.LoanCalculator.navGraphs.RootNavigator
import com.foresko.LoanCalculator.ui.components.LoanCalculator
import com.foresko.LoanCalculator.ui.components.TopAppBar
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.StateFlow

@Destination
@RootNavGraph(start = true)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    MainScreenContent(
        offers = viewModel.offers,
        viewModel = viewModel,
        rootNavigator = rootNavigator
    )
}

@Composable
fun MainScreenContent(
    offers: StateFlow<List<MicroLoan>?>,
    viewModel: MainViewModel,
    rootNavigator: RootNavigator
) {
    val focusManager = LocalFocusManager.current

    val offers by offers.collectAsState()
    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = { TopAppBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                item {
                    LoanCalculator(
                        sumAmount = viewModel.sumAmount,
                        changeSumAmount = { viewModel.changeSumAmount(it) },
                        percentRate = viewModel.percentRate,
                        changePercentRate = { viewModel.changePercentRate(it) },
                        startDate = viewModel.startDate,
                        changeStartDate = { viewModel.changeStartDate(it) },
                        finalDate = viewModel.finalDate,
                        changeFinalDate = { viewModel.changeFinalDate(it) },
                        rootNavigator = rootNavigator
                    )
                }
            }
        }
    }
}
