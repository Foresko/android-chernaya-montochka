package com.foresko.CalculatorLite.ui.destinations.selectCountry

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.foresko.CalculatorLite.R
import com.foresko.CalculatorLite.ui.navGraphs.RootNavigator
import com.foresko.CalculatorLite.ui.destinations.destinations.MainScreenDestination
import com.foresko.CalculatorLite.utils.countryNamesMap

@Destination
@RootNavGraph
@Composable
fun SelectCountry(
    viewModel: SelectCountryViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    SelectCountryContent(
        viewModel = viewModel,
        rootNavigator = rootNavigator
    )
}

@Composable
private fun SelectCountryContent(
    viewModel: SelectCountryViewModel,
    rootNavigator: RootNavigator
) {
    val activeCountry by viewModel.activeCountry.collectAsState()

    val countrys by viewModel.country.collectAsState()

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .statusBarsPadding(),
        containerColor = Color.White,
        topBar = { TopAppBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            countrys?.let { list ->
                LazyColumn {
                    items(list) { country ->
                        val countryName = countryNamesMap[country.code] ?: country.code
                        CountryItem(
                            iconUri = country.flagUrl,
                            name = countryName,
                            isActive = activeCountry == country.code,
                            onItemClicked = { viewModel.setActiveCountry(country.code) }
                        )
                    }

                    item {
                        Box(modifier = Modifier
                            .size(30.dp)
                            .background(Color.White)
                            .clickable { viewModel.completeFirstStartProcess() })
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                ButtonContinue(
                    viewModel = viewModel,
                    rootNavigator = rootNavigator
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar() {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.take_country),
                fontSize = 17.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight(600)
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = Color.Black
        )
    )
}

@Composable
fun CountryItem(
    iconUri: String,
    name: String,
    isActive: Boolean,
    onItemClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClicked)
            .padding(horizontal = 16.dp, vertical = 15.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(Uri.parse(iconUri)),
            contentDescription = null,
            modifier = Modifier
                .size(34.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = name,
            color = Color.Black,
            fontSize = 16.sp,
            lineHeight = 19.sp,
            fontWeight = FontWeight(600),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f, true)
        )

        if (isActive)
            Image(
                painter = painterResource(id = R.drawable.active_check_box),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
            )
        else
            Image(
                painter = painterResource(id = R.drawable.check_box),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
            )
    }

    Divider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = Color.Black.copy(alpha = 0.2f),
        thickness = 0.75f.dp,
    )
}

@Composable
private fun ButtonContinue(
    viewModel: SelectCountryViewModel,
    rootNavigator: RootNavigator
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF1F611),
                        Color(0xFF29D0AF),
                        Color(0xFF29D0AF),
                        Color(0xFF29D0AF),
                        Color(0xFF0DC242)
                    ),
                    start = Offset(-0.9f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0.9f)
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                rootNavigator.navigate(MainScreenDestination())
                viewModel.completeFirstStartProcess()
            }
    ) {
        Text(
            modifier = Modifier.padding(vertical = 19.dp),
            text = stringResource(R.string.continue_button),
            color = Color.White,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight(600),
        )
    }
}
