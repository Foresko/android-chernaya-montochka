package com.foresko.CalculatorLite.ui.destinations

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.CalculatorLite.R
import com.foresko.CalculatorLite.ui.components.LoanCalculator
import com.foresko.CalculatorLite.ui.components.MicroLoanOffer
import com.foresko.CalculatorLite.ui.components.TopAppBar
import com.foresko.CalculatorLite.ui.enumClass.FilterType
import com.foresko.CalculatorLite.ui.enumClass.TimePeriod
import com.foresko.CalculatorLite.ui.navGraphs.RootNavigator
import com.foresko.CalculatorLite.utils.NetworkConnectionErrorSnackBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph


@Destination
@RootNavGraph(start = true)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    MainScreenContent(
        viewModel = viewModel,
        rootNavigator = rootNavigator,
        isNetworkConnectionError = viewModel.isNetworkConnectionError,
        changeNetworkConnectionErrorState =viewModel::changeNetworkConnectionErrorState,
        modifier = Modifier
            .zIndex(3f)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    viewModel: MainViewModel,
    rootNavigator: RootNavigator,
    isNetworkConnectionError: Boolean,
    changeNetworkConnectionErrorState: (Boolean) -> Unit,
    modifier: Modifier

) {
    val storeInfo = viewModel.storeInfo.collectAsState().value

    val activeCountryCode = viewModel.activeCountryCode.collectAsState().value

    val countries = viewModel.countries.collectAsState().value

    val activeCountryFlagUrl = countries?.find { it.code == activeCountryCode }?.flagUrl ?: ""

    val selectedPeriod by viewModel.selectedPeriod.observeAsState(initial = TimePeriod.DAY)

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
        backgroundColor = Color.White,
        topBar = { TopAppBar(
            iconUri = activeCountryFlagUrl,
            rootNavigator = rootNavigator
        ) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
        ) {
            if (storeInfo != null) {
                LazyColumn(
                    modifier = Modifier
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
                            rootNavigator = rootNavigator,
                            daysDifference = viewModel::daysDifference,
                            selectedPeriod = selectedPeriod,
                            onSelectedPeriodChange = viewModel::setSelectedPeriod,
                        )
                    }

                    stickyHeader {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                        ) {
                            Spacer(modifier = Modifier.height(18.dp))

                            AppNameAndCountries()

                            Spacer(modifier = Modifier.height(18.dp))

                            Filters(
                                activeFilter = viewModel.activeFilterType,
                                onActiveFilterChange = viewModel::onActiveFilterChange,
                                filterList = FilterType.values().toList()
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    items(storeInfo) { offer ->
                        MicroLoanOffer(offer = offer)
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            } else {
                NetworkConnectionErrorSnackBar(
                    isNetworkConnectionError = isNetworkConnectionError,
                    changeNetworkConnectionState = { changeNetworkConnectionErrorState(true) },
                    modifier = modifier
                        .padding(top = 20.dp)
                )

                LoadingBox()
            }
        }
    }
}

@Composable
private fun AppNameAndCountries() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.best_deals),
            color = Color.Black,
            fontSize = 18.sp,
            lineHeight = 19.sp,
            fontWeight = FontWeight(600)
        )
    }
}

@Composable
private fun Filters(
    activeFilter: FilterType,
    onActiveFilterChange: (FilterType) -> Unit,
    filterList: List<FilterType>
) {
    LazyRow {
        item {
            Spacer(modifier = Modifier.width(14.dp))
        }

        items(filterList) { filter ->
            FilterBox(
                isActive = filter == activeFilter,
                name = filter.labelId
            ) {
                onActiveFilterChange(filter)
            }
        }

        item {
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
private fun FilterBox(
    isActive: Boolean,
    @StringRes name: Int,
    onClick: () -> Unit,
) {
    val (backgroundColor, borderColor, nameColor) = remember(isActive) {
        if (isActive) {
            Triple(Color(0xFFE5E6FF), Color(0xFFB9B9FF), Color.Black)
        } else {
            Triple(Color.White, Color(0xFFF2F2F8), Color(0xB3343D4E))
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .defaultMinSize(minHeight = 36.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(30.dp))
            .background(color = backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false)
            ) {
                onClick()
            }
    ) {
        Text(
            text = stringResource(name),
            color = nameColor,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun LoadingBox() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = Color.Black,
            strokeWidth = 3.dp,
            modifier = Modifier
                .size(46.dp)
        )
    }
}
