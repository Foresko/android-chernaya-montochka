package com.foresko.CalculatorLite.ui.destinations

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.foresko.CalculatorLite.R
import com.foresko.CalculatorLite.ui.components.LoanCalculator
import com.foresko.CalculatorLite.ui.components.MicroLoanOffer
import com.foresko.CalculatorLite.ui.enumClass.FilterType
import com.foresko.CalculatorLite.ui.enumClass.TimePeriod
import com.foresko.CalculatorLite.ui.navGraphs.RootNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.launch


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
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    viewModel: MainViewModel,
    rootNavigator: RootNavigator
) {
    val storeInfo = viewModel.storeInfo.collectAsState().value

    val selectedPeriod by viewModel.selectedPeriod.collectAsState(initial = TimePeriod.DAY)

    val scrollState = rememberLazyListState()

    val showButton = remember { derivedStateOf { scrollState.firstVisibleItemIndex > 0 } }

    val coroutineScope = rememberCoroutineScope()

    val isAppNameVisible = remember {
        derivedStateOf {
            scrollState.layoutInfo.visibleItemsInfo.any { it.key == "AppNameAndCountries" }
        }
    }

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
        backgroundColor = Color.White,
        topBar = {
            TopAppBar(isAppNameVisible.value)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (showButton.value) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 36.dp)
                        .shadow(2.dp, shape = RoundedCornerShape(16.dp))
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFEDA534),
                                    Color(0xFFFD8A53),
                                ),
                                start = Offset(-0.9f, Float.POSITIVE_INFINITY),
                                end = Offset(Float.POSITIVE_INFINITY, 0.9f)
                            )
                        )
                        .clickable {
                            coroutineScope.launch {
                                scrollState.animateScrollToItem(0)
                            }
                        }
                        .zIndex(2f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(15.dp),
                        tint = Color.White
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.zIndex(1f),
                state = scrollState
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

                item { Spacer(modifier = Modifier.height(32.dp)) }

                item(key = "AppNameAndCountries") {
                    AppNameAndCountries()
                }

                if (storeInfo != null) {
                    stickyHeader {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                        ) {
                            Spacer(modifier = Modifier.height(18.dp))

                            Filters(
                                activeFilter = viewModel.activeFilterType,
                                onActiveFilterChange = viewModel::onActiveFilterChange,
                                filterList = FilterType.entries
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    items(storeInfo) { offer ->
                        MicroLoanOffer(offer = offer)
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                } else {
                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.height(50.dp))

                            CircularProgressIndicator(
                                color = Color.Black,
                                strokeWidth = 3.dp,
                                modifier = Modifier
                                    .size(46.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = stringResource(id = R.string.check_internet),
                                color = Color.Black,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(500),
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    isAppNameVisible: Boolean
) {
    val transition = updateTransition(targetState = isAppNameVisible, label = "Title Transition")

    val titleAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "Title Alpha"
    ) { state ->
        if (state) 1f else 0f
    }

    CenterAlignedTopAppBar(
        title = {
            Box {

                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(600),
                    modifier = Modifier
                        .alpha(titleAlpha)
                        .align(Alignment.Center)
                )

                Text(
                    text = stringResource(R.string.best_deals),
                    color = Color.Black,
                    fontSize = 18.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight(600),
                    modifier = Modifier
                        .alpha(1f - titleAlpha)
                        .align(Alignment.Center)
                )
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color(0xFFFBFAFF),
            titleContentColor = Color.Black
        )
    )
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
                indication = ripple(bounded = false)
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
